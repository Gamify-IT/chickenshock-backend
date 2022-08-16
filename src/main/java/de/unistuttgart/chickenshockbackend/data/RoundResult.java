package de.unistuttgart.chickenshockbackend.data;

import java.util.UUID;
import javax.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Entity
@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoundResult {

  @Id
  @GeneratedValue(generator = "uuid")
  UUID id;

  @ManyToOne
  Question question;

  String answer;

  public RoundResult(Question question, String answer) {
    this.question = question;
    this.answer = answer;
  }
}
