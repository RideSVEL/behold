package serejka.telegram.behold.service;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.ActionType;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.media.InputMedia;
import org.telegram.telegrambots.meta.api.objects.media.InputMediaPhoto;
import serejka.telegram.behold.config.APIConfig;
import serejka.telegram.behold.logic.bot.Bot;
import serejka.telegram.behold.logic.enums.Commands;
import serejka.telegram.behold.models.Movie;

@Slf4j
@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ReplyToUserService {

  ParserService parserService;
  Bot superBot;
  UserService userService;

  public ReplyToUserService(ParserService parserService, @Lazy Bot superBot, UserService userService) {
    this.parserService = parserService;
    this.superBot = superBot;
    this.userService = userService;
  }

  public String replyHelp() {
    return "Я тебе всегда помогу!";
  }

  public String replyStart(Message message) {
    userService.checkAndSave(message);
    return "Привіт, " + message.getFrom().getFirstName()
        + "!\nТи потрапив у найкращий бот з підбору фільмів :)" +
        "\nУсі необхідні функції ти зможеш знайти на клавіатурі знизу" +
        "\nЯкщо виникнуть питання, використовуй клавішу \"Допомога\", або надішліть команду /help" +
        "\nУспіхів! \uD83D\uDE1C";
  }

  public String replyReview() {
    return "Я радий, що ти вирішив залишити відгук про наш бот," +
        " надішліть свої побажання\uD83D\uDE0C" +
        "\nАбо можеш скасувати операцію командою - /cancel\uD83D\uDE15";
  }

  public String replySearch() {
    return "Давай знайдемо потрібний тобі фільм\uD83D\uDE09" +
        "\nПиши назву, або можеш скасувати операцію, натиснувши відповідну клавішу на клавіаутрі)";
  }

  public String replyDescription() {
    return "Я допоможу тобі знайти фільм за твоїм описом\uD83D\uDE0B" +
        "\nПостарайся повноцінно описати необхідний фільм із дрібними відомими деталями. " +
        "\nАбо можеш скасувати операцію, натиснувши відповідну клавішу на клавіаутрі)";
  }

  public String replyError() {
    return "Щось не так, вибач\uD83D\uDE1E\nДавай користуватися кнопками☺️";
  }

  public String replyListMovies(List<Movie> movies, Commands commands) {
    String reply;
    reply = replyError();
    if (movies != null) {
      log.info("Get movie: {}", movies);
      StringBuilder sb = new StringBuilder();
      switch (commands) {
        case TOPDAY -> sb.append("<em>Популярні фільми на сьогодні:</em>");
        case PERSONAL_LIST -> sb.append("<em>Фільми підібрані за твоїми персональними вподобаннями:</em>\n" +
            "Якщо ти бажаєш отримати інший список, просто скористуйся командою ще раз, і ми з помічником підберемо тобі нових за твоїми інтересами)");
        case TOPWEEK -> sb.append("<em>Найкраще за тиждень:</em>");
        case TOP -> sb.append("<em>Користуються попитом великий проміжок часу:</em>");
        case SEARCH -> sb.append("Якщо результати відсутні або не задовольняють пошуки, повтори введення\uD83D\uDE09" +
            "\nТакож можеш скасувати операцію, натиснувши клавішу на клавіатурі" +
            "\n\n<em>Знайшлося:</em>");
        case BOOKMARKS -> sb.append("<em>Ваши закладки</em>\uD83D\uDCBC" +
            "\nДля видалення фільму із закладок, скористайся відповідною клавішею" +
            "\n\n<em>Знайшлося:</em>");
      }
      for (int i = 0; i < Math.min(movies.size(), 5); i++) {
        Movie movie = movies.get(i);
        sb.append("\n\n<b>").append(i + 1).append(". <em>").append(movie.getTitle()).append(" (")
            .append(movie.getYear()).append(")</em></b>").append(" | ").append(movie.getVoteAverage())
            .append("\nЖанр: ");
        for (String genre : movie.getGenres()) {
          sb.append(genre).append(", ");
        }
        sb.delete(sb.length() - 2, sb.length() - 1);
      }
      sb.append("\n\nДля отримання подробиць фільму скористайтесь однією з кнопок нижче:");
      return sb.toString();
    }
    return reply;
  }

  public String replyCheckMessageToUser(Message message) {
    superBot.sendChatActionUpdate(message.getChatId(), ActionType.TYPING);
    return "Данная функция находится на стадии тестирования, поэтому возможны небольшие задержки\uD83D\uDE22" +
        "\nНадо чуточку потерпеть\uD83D\uDC40";
  }

  public String senWaiting(Message message) {
    superBot.sendChatActionUpdate(message.getChatId(), ActionType.TYPING);
    return "Кілька секунд очікування) Підбираємо фільм...";
  }

  public String replyMovie(long chatId, String filmId) {
    String reply;
    try {
      reply = "Щось не вдалося знайти такий фільм...";
      Movie movie = parserService.parseMovie(Integer.parseInt(filmId));
      if (movie != null) {
        superBot.sendChatActionUpdate(chatId, ActionType.UPLOADPHOTO);
        log.info("Get movie: {}", movie);
        List<InputMedia> list = new ArrayList<>();
        for (String s : movie.getPathToImages()) {
          InputMediaPhoto inputMediaPhoto = new InputMediaPhoto();
          InputStream input = new URL(APIConfig.getPathToImage(s)).openStream();
          inputMediaPhoto.setMedia(input, s);
          list.add(inputMediaPhoto);
        }
        superBot.sendMediaGroup(chatId, list);
        superBot.sendChatActionUpdate(chatId, ActionType.TYPING);
        reply = showMovie(movie);
      }
    } catch (NumberFormatException e) {
      reply = "Щось не вдалося знайти такий фільм...";
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    return reply;
  }

  private String showMovie(Movie movie) {
    StringBuilder sb = new StringBuilder("<b><u>"
        + movie.getTitle() + " (" + movie.getYear() + ")" + "</u></b>\n\n");
    sb.append("Країна: ");
    for (String s : movie.getCountry()) {
      sb.append(s).append(", ");
    }
    sb.delete(sb.length() - 2, sb.length() - 1);
    sb.append("\n<em>Оцінка: ").append(movie.getVoteAverage()).append(" (")
        .append(movie.getVotes()).append(")</em>");
    sb.append("\nЖанр: ");
    for (String s : movie.getGenres()) {
      sb.append(s).append(", ");
    }
    sb.delete(sb.length() - 2, sb.length() - 1);
    sb.append("\nПрем'єра: ").append(movie.getReleaseDate());
    sb.append("\nБюджет: ").append(movie.getBudget()).append("$");
    sb.append("\nТривалість: ").append(movie.getRuntime()).append(" мин.");
    sb.append("\nОригінальна назва: ").append(movie.getOriginalTitle());
    sb.append("\n\n").append(movie.getOverview());
    return sb.toString();
  }
}



