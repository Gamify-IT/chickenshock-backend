package com.moorhuhnservice.moorhuhnservice.data;

import java.util.List;
import java.util.Set;
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
public class Question {

  @Id
  @GeneratedValue(generator = "uuid")
  UUID id;

  String text;
  String rightAnswer;

  @ElementCollection
  Set<String> wrongAnswers;

  public Question(String text, String rightAnswer, Set<String> wrongAnswers) {
    this.text = text;
    this.rightAnswer = rightAnswer;
    this.wrongAnswers = wrongAnswers;
  }
}
