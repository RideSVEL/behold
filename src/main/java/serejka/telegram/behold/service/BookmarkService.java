package serejka.telegram.behold.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import serejka.telegram.behold.models.Bookmark;
import serejka.telegram.behold.models.Movie;
import serejka.telegram.behold.models.User;
import serejka.telegram.behold.repository.BookmarkRepository;

@RequiredArgsConstructor
@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BookmarkService {

  BookmarkRepository bookmarkRepository;
  UserService userService;
  ParserService parserService;

  public String saveBookmark(Long userId, Long movieId) {
    Optional<Bookmark> bookmark = checkExistBookmark(userId, movieId);
    if (bookmark.isEmpty()) {
      User userByUserId = userService.findUserByUserId(userId);
      bookmarkRepository.save(new Bookmark(userByUserId, movieId));
      return "Фильм был успешно добавлен в закладки\uD83D\uDE4C";
    } else {
      return "Этот фильм уже находится в ваших закладках\uD83D\uDE45\u200D♂️";
    }
  }

  public String deleteBookmark(Long userId, Long movieId) {
    Optional<Bookmark> bookmark = checkExistBookmark(userId, movieId);
    if (bookmark.isPresent()) {
      bookmarkRepository.delete(bookmark.get());
      return "Фильм был удален из закладок❌";
    } else {
      return "Этот фильм отсутствует в ваших закладках\uD83D\uDE45\u200D♂️";
    }
  }

  public Optional<Bookmark> checkExistBookmark(Long userId, Long movieId) {
    User userByUserId = userService.findUserByUserId(userId);
    return bookmarkRepository.findBookmarkByUserAndMovieId(userByUserId, movieId);
  }

  public List<Bookmark> findAllBookmarksByUserId(Long userId) {
    return bookmarkRepository.findAllByUser(userService.findUserByUserId(userId));
  }

  public List<Movie> findAllMoviesByUserBookmarks(Long userId) {
    return findAllBookmarksByUserId(userId).stream()
        .map(bookmark -> parserService.parseMovie(Integer.parseInt(String.valueOf(bookmark.getMovieId()))))
        .collect(Collectors.toList());
  }
}
