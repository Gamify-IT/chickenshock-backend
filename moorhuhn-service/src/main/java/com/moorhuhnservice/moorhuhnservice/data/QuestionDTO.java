package com.moorhuhnservice.moorhuhnservice.data;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class QuestionDTO {
    String configuration;
    String questionText;
    String rightAnswer;
    // Should be an Array
    String wrongAnswerOne;
    String wrongAnswerTwo;
    String wrongAnswerThree;
    String wrongAnswerFour;
}
