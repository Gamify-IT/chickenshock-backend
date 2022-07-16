package com.moorhuhnservice.moorhuhnservice.data;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionDTO {

  String text;
  String rightAnswer;
  List<String> wrongAnswers;
}
