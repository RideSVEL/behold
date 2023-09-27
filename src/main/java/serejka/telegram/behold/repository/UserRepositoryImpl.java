package serejka.telegram.behold.repository;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import serejka.telegram.behold.models.User;

@Repository
public class UserRepositoryImpl {

  @PersistenceContext
  private EntityManager entityManager;

  public List<User> findOrderedByIdLimitingBy(Long id, Integer limit) {
    return entityManager.createQuery("SELECT u FROM User u where u.id > ?1 ORDER BY u.id",
        User.class).setParameter(1, id).setMaxResults(limit).getResultList();
  }
}
