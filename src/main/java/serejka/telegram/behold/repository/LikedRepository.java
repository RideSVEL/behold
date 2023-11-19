package serejka.telegram.behold.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import serejka.telegram.behold.models.Liked;
import serejka.telegram.behold.models.User;

@Repository
public interface LikedRepository extends JpaRepository<Liked, Long> {

  Optional<Liked> findLikedByUser(User user);
  Optional<Liked> findByMovieId(Long movieId);

  List<Liked> findLikedByUser(User user, Pageable pageable);

  List<Liked> findAllByUser(User user);
}
