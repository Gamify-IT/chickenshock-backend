package com.moorhuhnservice.moorhuhnservice.data;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "questions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    long id;

    @Column(nullable = false)
    String configuration;

    @Column(nullable = false)
    String questionText;

    @Column(nullable = false)
    String rightAnswer;

    @Column(nullable = false)
    String wrongAnswerOne;

    @Column(nullable = false)
    String wrongAnswerTwo;

    @Column(nullable = false)
    String wrongAnswerThree;

    @Column(nullable = false)
    String wrongAnswerFour;

    public Question(String configuration, String questionText, String rightAnswer, String wrongAnswerOne, String wrongAnswerTwo, String wrongAnswerThree, String wrongAnswerFour) {
        this.configuration = configuration;
        this.questionText = questionText;
        this.rightAnswer = rightAnswer;
        this.wrongAnswerOne = wrongAnswerOne;
        this.wrongAnswerTwo = wrongAnswerTwo;
        this.wrongAnswerThree = wrongAnswerThree;
        this.wrongAnswerFour = wrongAnswerFour;
    }

}
