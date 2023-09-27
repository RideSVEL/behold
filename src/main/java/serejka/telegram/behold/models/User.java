package serejka.telegram.behold.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.FieldBridge;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.TermVector;
import org.hibernate.search.bridge.builtin.IntegerBridge;

@Indexed
@Data

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Field(termVector = TermVector.YES)
  @FieldBridge(impl = IntegerBridge.class)
  @Column(nullable = false)
  private Long userId;
  @Field(termVector = TermVector.YES)
  private String userName;
  @Field(termVector = TermVector.YES)
  private String firstName;
  @Field(termVector = TermVector.YES)
  private String lastName;
  private int countOfUse;

  public User(Long userId, String userName, String firstName, String lastName, int countOfUse) {
    this.userId = userId;
    this.userName = userName;
    this.firstName = firstName;
    this.lastName = lastName;
    this.countOfUse = countOfUse;
  }
}
