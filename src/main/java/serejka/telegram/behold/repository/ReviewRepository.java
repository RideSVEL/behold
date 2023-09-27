package serejka.telegram.behold.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import serejka.telegram.behold.models.Review;

public interface ReviewRepository extends JpaRepository<Review, Long> {

  List<Review> findAllByViewOrderByIdDesc(int view);

  Long countAllByView(int view);
}
