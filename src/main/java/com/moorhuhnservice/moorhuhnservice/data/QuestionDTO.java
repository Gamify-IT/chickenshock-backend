package com.moorhuhnservice.moorhuhnservice.data;

import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionDTO {

  @Nullable
  UUID id;

  String text;
  String rightAnswer;
  Set<String> wrongAnswers;

  public QuestionDTO(final String text, final String rightAnswer, final Set<String> wrongAnswers) {
    this.text = text;
    this.rightAnswer = rightAnswer;
    this.wrongAnswers = wrongAnswers;
  }

  public boolean equalsContent(final Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    final QuestionDTO that = (QuestionDTO) o;
    return (
      Objects.equals(text, that.text) &&
      Objects.equals(rightAnswer, that.rightAnswer) &&
      Objects.equals(wrongAnswers, that.wrongAnswers)
    );
  }
}
