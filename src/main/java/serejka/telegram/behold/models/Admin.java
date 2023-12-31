package serejka.telegram.behold.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@Entity(name = "admins")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Admin {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  Long id;

  @Column(unique = true, nullable = false)
  String username;

  @ToString.Exclude
  @Column(nullable = false)
  String password;
}
