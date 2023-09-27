package serejka.telegram.behold.logic.commands.msgCmd;

import java.util.Collections;
import java.util.List;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import serejka.telegram.behold.logic.commands.MessageCommand;
import serejka.telegram.behold.logic.enums.Commands;
import serejka.telegram.behold.models.Movie;
import serejka.telegram.behold.service.BookmarkService;
import serejka.telegram.behold.service.MovieService;
import serejka.telegram.behold.service.ReplyToUserService;
import serejka.telegram.behold.service.SendMessageService;

@Slf4j
@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ShowBookmarksCmd implements MessageCommand {

  BookmarkService bookmarkService;
  MovieService movieService;
  ReplyToUserService replyService;
  SendMessageService messageService;

  @Override
  public SendMessage generateMessage(Message message) {
    List<Movie> movies = bookmarkService
        .findAllMoviesByUserBookmarks(message.getFrom().getId());
    Collections.reverse(movies);
    log.info("Movie send");
    return messageService.sendMsg(message.getFrom().getId(),
        replyService.replyListMovies(movies, getMyCommand()),
        movieService.getInlineMessageButtons(movies, true));
  }

  @Override
  public Commands getMyCommand() {
    return Commands.BOOKMARKS;
  }
}
