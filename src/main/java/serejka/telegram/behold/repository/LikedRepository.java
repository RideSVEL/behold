package serejka.telegram.behold.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import serejka.telegram.behold.models.Liked;

@Repository
public interface LikedRepository extends JpaRepository<Liked, Long> {
  Optional<Liked> findByMovieId(Long movieId);

  List<Liked> findLikedByUserId(Long user_id);
}
