package serejka.telegram.behold.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@Entity
@Table(name = "statistics")
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Stats {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  long id;
  int count;
  @Column(unique = true)
  String commandName;
}
