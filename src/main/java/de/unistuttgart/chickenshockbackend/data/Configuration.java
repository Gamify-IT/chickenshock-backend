package de.unistuttgart.chickenshockbackend.data;

import java.util.Set;
import java.util.UUID;
import javax.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;

@Entity
@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Configuration {

  @Id
  @GeneratedValue(generator = "uuid")
  UUID id;

  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  Set<Question> questions;

  @Value("${game.time}")
  int time;

  public Configuration(final Set<Question> questions, final int time) {
    this.questions = questions;
    this.time = time;
  }

  public void addQuestion(final Question question) {
    this.questions.add(question);
  }

  public void removeQuestion(final Question question) {
    this.questions.remove(question);
  }

  public void setTime(final int time) {
    this.time = time;
  }
}
