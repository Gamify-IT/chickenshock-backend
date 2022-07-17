package com.moorhuhnservice.moorhuhnservice.data;

import java.util.List;
import java.util.Objects;
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
  @GeneratedValue(strategy = GenerationType.AUTO)
  long id;

  String text;
  String rightAnswer;

  @ElementCollection
  List<String> wrongAnswers;

  public Question(String text, String rightAnswer, List<String> wrongAnswers) {
    this.text = text;
    this.rightAnswer = rightAnswer;
    this.wrongAnswers = wrongAnswers;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Question question = (Question) o;
    return id == question.id && Objects.equals(text, question.text);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, text);
  }
}
