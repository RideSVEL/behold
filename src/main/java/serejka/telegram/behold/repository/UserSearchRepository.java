package serejka.telegram.behold.repository;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.lucene.search.Query;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.FullTextQuery;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.springframework.stereotype.Repository;
import serejka.telegram.behold.models.User;

@Repository
public class UserSearchRepository {

  @PersistenceContext
  private EntityManager entityManager;

  public List<User> searchUsers(String text) {
    FullTextEntityManager fullTextEntityManager =
        Search.getFullTextEntityManager(entityManager);
    QueryBuilder queryBuilder = fullTextEntityManager.getSearchFactory()
        .buildQueryBuilder().forEntity(User.class).get();

    Query query = queryBuilder
        .keyword()
        .wildcard()
        .onFields("userName", "firstName", "lastName", "userId")
        .matching("*" + text + "*")
        .createQuery();

    FullTextQuery fullTextQuery =
        fullTextEntityManager.createFullTextQuery(query, User.class);

    return (List<User>) fullTextQuery.getResultList();
  }
}
