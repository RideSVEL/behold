package serejka.telegram.behold.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import serejka.telegram.behold.models.Bookmark;
import serejka.telegram.behold.models.User;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {

  Optional<Bookmark> findBookmarkByUserAndMovieId(User user, Long movieId);

  List<Bookmark> findAllByUser(User user);
}
