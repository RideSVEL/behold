package serejka.telegram.behold.logic.commands.stateCmd;

import java.util.Optional;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import serejka.telegram.behold.cache.UserDataCache;
import serejka.telegram.behold.logic.commands.StateCommand;
import serejka.telegram.behold.logic.enums.BotState;
import serejka.telegram.behold.logic.enums.Commands;
import serejka.telegram.behold.models.AIMovieResponse;
import serejka.telegram.behold.models.Movie;
import serejka.telegram.behold.service.AIService;
import serejka.telegram.behold.service.KeyboardService;
import serejka.telegram.behold.service.MovieService;
import serejka.telegram.behold.service.ParserService;
import serejka.telegram.behold.service.ReplyToUserService;
import serejka.telegram.behold.service.SendMessageService;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DescriptionFilmState implements StateCommand {

  MovieService movieService;
  AIService aiService;
  UserDataCache userDataCache;
  KeyboardService keyboardService;
  ParserService parserService;
  ReplyToUserService replyToUserService;
  SendMessageService sendMsg;

  @Override
  public SendMessage generateMessage(Message message) {
    if (isCancel(message)) {
      return returnCancel(message);
    }

    return aiService.getMovieResponse(message.getText())
        .map(this::getRequiredFilm)
        .flatMap(inner -> inner)
        .map(mov -> replyMovieByid(message, mov))
        .orElse(replyMovieByid(message, new Movie()));
  }

  private SendMessage returnCancel(Message message) {
    userDataCache.deleteStateUser(message.getChatId());
    return keyboardService.getKeyboard(message.getChatId(),
        "Увидимся в следующий раз\uD83D\uDE0A", Commands.START);
  }

  private boolean isCancel(Message message) {
    return message.getText().equals("/cancel")
        || message.getText().equals("Вернуться\uD83D\uDE15");
  }

  @NotNull
  private Optional<Movie> getRequiredFilm(AIMovieResponse response) {
    return parserService.getListMoviesBySearch(response.getName())
        .stream().findFirst();
  }

  private SendMessage replyMovieByid(Message message, Movie movie) {
    String reply = replyToUserService.replyMovie(message.getChatId(), String.valueOf(movie.getId()));
    if (reply.equals("Что-то не получилось найти такой фильм...")) {
      return sendMsg.sendMsg(message.getChatId(), reply);
    }
    return sendMsg.sendMsg(message.getChatId(), reply,
        keyboardService.getInlineMessageButtonForFilm(movie.getId(),
            message.getChatId()));
  }

  @Override
  public BotState getMyCommand() {
    return BotState.DESCRIPTION;
  }
}
