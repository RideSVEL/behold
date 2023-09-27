package serejka.telegram.behold.models;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@Entity
@Table(name = "bookmarks")
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Bookmark implements Serializable {

  public Bookmark(User user, Long movieId) {
    this.user = user;
    this.movieId = movieId;
  }

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  Long id;

  @ManyToOne
  @JoinColumn(name = "user_id")
  User user;

  Long movieId;
}
