package serejka.telegram.behold.logic.commands.msgCmd;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.telegram.telegrambots.meta.api.methods.ActionType;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.thymeleaf.util.MapUtils;
import serejka.telegram.behold.logic.bot.Bot;
import serejka.telegram.behold.logic.commands.MessageCommand;
import serejka.telegram.behold.logic.enums.Commands;
import serejka.telegram.behold.models.AIMovieResponse;
import serejka.telegram.behold.models.Audit;
import serejka.telegram.behold.models.Liked;
import serejka.telegram.behold.models.Movie;
import serejka.telegram.behold.models.User;
import serejka.telegram.behold.repository.AuditRepository;
import serejka.telegram.behold.repository.LikedRepository;
import serejka.telegram.behold.service.AIService;
import serejka.telegram.behold.service.MovieService;
import serejka.telegram.behold.service.ParserService;
import serejka.telegram.behold.service.ReplyToUserService;
import serejka.telegram.behold.service.SendMessageService;
import serejka.telegram.behold.service.UserService;

@Slf4j
@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PersonalListCmd implements MessageCommand {
  ReplyToUserService replyService;
  Bot superBot;
  UserService userService;
  AuditRepository auditRepository;
  LikedRepository likedRepository;
  ParserService parserService;
  AIService aiService;
  SendMessageService messageService;
  MovieService movieService;

  Map<String, String> keyToSkip = Map.of("LIKED", "UNLIKED");

  @Autowired
  public PersonalListCmd(
      ReplyToUserService replyService,
      @Lazy Bot superBot, UserService userService, AuditRepository auditRepository,
      LikedRepository likedRepository, ParserService parserService, AIService aiService,
      SendMessageService messageService,
      MovieService movieService) {
    this.replyService = replyService;
    this.superBot = superBot;
    this.userService = userService;
    this.auditRepository = auditRepository;
    this.likedRepository = likedRepository;
    this.parserService = parserService;
    this.aiService = aiService;
    this.messageService = messageService;
    this.movieService = movieService;
  }

  @Override
  public SendMessage generateMessage(Message message) {
    superBot.sendDiceForWaiting(message.getChatId());
    superBot.sendChatActionUpdate(message.getChatId(), ActionType.TYPING);
    List<Movie> personalMoviesList;
    try {
      personalMoviesList = getPersonalMoviesList(message);
    } catch (Exception e) {
      log.error("Error during generating personal list", e);
      return messageService.sendMsg(message.getFrom().getId(),
          "Нажаль, наразі ми не маємо достатньо інформації про твої вподобання або помічник втомився\uD83D\uDE43\n" +
              "Ти можеш скористатися іншим функціоналом та повернутися згодом\uD83D\uDE1A");
    }


    return messageService.sendMsg(message.getFrom().getId(),
        replyService.replyListMovies(personalMoviesList, getMyCommand()),
        movieService.getInlineMessageButtons(personalMoviesList, false));
  }

  @NotNull
  private List<Movie> getPersonalMoviesList(Message message) {
    User userByUserId = userService.findUserByUserId(message.getChatId());
    Long id = userByUserId.getId();
    List<Audit> byUserId = auditRepository.findByUserId(id);
    Map<String, List<Audit>> collect = byUserId.stream().collect(Collectors.groupingBy(Audit::getAction));

    var stringListHashMap = new HashMap<String, List<Long>>();

    Map<Boolean, List<Liked>> liked = likedRepository.findLikedByUserId(id).stream()
        .collect(Collectors.groupingBy(Liked::getLiked));

    collect.keySet().stream().filter(key -> !keyToSkip.containsKey(key))
        .forEach(key -> {
      List<Audit> audits = collect.get(key);
      List<Long> collect1 = audits.stream().map(Audit::getMovieId)
          .distinct().collect(Collectors.toList());
      stringListHashMap.put(key, collect1);
    });

    List<Long> likedMovieIds = collectMovieIds(liked, true);
    stringListHashMap.put("LIKED", likedMovieIds);
    List<Long> unlikedMovieIds = collectMovieIds(liked, false);
    stringListHashMap.put("UNLIKED", unlikedMovieIds);

    List<Long> preferredMovieIds = new ArrayList<>();
    List<Long> unrespectableMovieIds = new ArrayList<>();

    shuffle(likedMovieIds);
    List<Long> bookmark = stringListHashMap.get("BOOKMARK");
    shuffle(bookmark);

    addAllToCollection(preferredMovieIds, likedMovieIds);
    addAllToCollection(preferredMovieIds, bookmark);

    List<Long> movie = stringListHashMap.get("MOVIE");

    if (!CollectionUtils.isEmpty(movie)) {
      preferredMovieIds = distinctTrimToSize(preferredMovieIds, 4);
      addAllToCollection(preferredMovieIds, movie);
    }

    preferredMovieIds = distinctTrimToSize(preferredMovieIds, 5);

    addAllToCollection(unrespectableMovieIds, stringListHashMap.get("UNLIKED"));
    unrespectableMovieIds = distinctTrimToSize(unrespectableMovieIds, 3);

    List<String> likedNames = getMovieNames(preferredMovieIds);
    List<String> dislikedNames = getMovieNames(unrespectableMovieIds);

    List<AIMovieResponse> movieResponse = aiService.getMovieResponse(likedNames, dislikedNames);

    if (CollectionUtils.isEmpty(movieResponse)) {
      throw new IllegalArgumentException("Not found");
    }

    return movieResponse.parallelStream().map(this::getRequiredFilm)
        .flatMap(Optional::stream)
        .toList();
  }

  private void addAllToCollection(List<Long> destination, List<Long> source) {
    if (Objects.nonNull(destination) && !CollectionUtils.isEmpty(source)) {
      destination.addAll(source);
    }
  }

  private void shuffle(List<?> list) {
    if (!CollectionUtils.isEmpty(list)) {
      Collections.shuffle(list);
    }

  }

  private Optional<Movie> getRequiredFilm(AIMovieResponse response) {
    List<Movie> listMoviesBySearch = parserService.getListMoviesBySearch(response.getName());
    if (!CollectionUtils.isEmpty(listMoviesBySearch)) {
      listMoviesBySearch.sort(((o1, o2) -> -1 * Float.compare(o1.getVoteAverage(), o2.getVoteAverage())));
      listMoviesBySearch.sort(((o1, o2) -> -1 * o1.getVotes() - o2.getVotes()));
    }
    return listMoviesBySearch.stream().findFirst();
  }

  @NotNull
  private List<String> getMovieNames(List<Long> preferredMovieIds) {
    return getMoviesByIds(preferredMovieIds).parallelStream().
        map(Movie::getTitle).collect(Collectors.toList());
  }

  @NotNull
  private List<Movie> getMoviesByIds(List<Long> preferredMovieIds) {
    return preferredMovieIds.stream()
        .map(movieId -> parserService.parseMovie(Integer.parseInt(String.valueOf(movieId))))
        .toList();
  }

  private <T> List<T> distinctTrimToSize(List<T> list, int size) {
    List<T> collect = list.stream().distinct().collect(Collectors.toList());
    if (collect.size() > size) {
      return new ArrayList<>(collect.subList(0, size));
    } else {
      return new ArrayList<>(collect);
    }
  }

  @NotNull
  private List<Long> collectMovieIds(Map<Boolean, List<Liked>> entities, boolean liked) {
    if (!MapUtils.isEmpty(entities)) {
      List<Liked> likeds = entities.get(liked);
      if (!CollectionUtils.isEmpty(likeds)) {
        return likeds.stream().map(Liked::getMovieId).collect(Collectors.toList());
      }
    }
    return List.of();
  }

  @Override
  public Commands getMyCommand() {
    return Commands.PERSONAL_LIST;
  }
}
