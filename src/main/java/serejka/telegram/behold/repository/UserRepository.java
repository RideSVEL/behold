package serejka.telegram.behold.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import serejka.telegram.behold.models.User;

public interface UserRepository extends JpaRepository<User, Long> {

  boolean existsUserByUserId(Long userId);

  User findUserByUserId(Long userId);

  @Query(value = "select * from Users u where u.id > ?1 order by u.id limit 10",
      nativeQuery = true)
  List<User> findAllByIdGreaterThanOrderById(Long id);
}
