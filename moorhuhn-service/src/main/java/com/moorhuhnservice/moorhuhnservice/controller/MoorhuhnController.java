package com.moorhuhnservice.moorhuhnservice.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.moorhuhnservice.moorhuhnservice.BaseClasses.Question;
import com.moorhuhnservice.moorhuhnservice.repositories.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class MoorhuhnController {

    @Autowired
    QuestionRepository questionRepository;

    @PostMapping("/save-all-questions")
    public List<Question> saveAllQuestions(@RequestBody List<Question> questions) {
        System.out.println("try to save all "+questions.size()+" questions: ");
        return questionRepository.saveAll(questions);
    }

    @PostMapping("/save-first-test-question")
    public Question saveFirstTestQuestion() {
        System.out.println("try to save a static question");
        Question questionTest = new Question("xyz", "Frage1","AntwortRichtig","AntwortFalsch1","AntwortFalsch2","AntwortFalsch3","AntwortFalsch4");
        questionRepository.save(questionTest);
        return questionTest;
    }

    @DeleteMapping("delete-question-element-with-question/{question}")
    public Question deleteQuestionByQuestion(@PathVariable String question){
        return questionRepository.deleteByQuestion(question);
    }

    @PutMapping("put-question-element-with-question/{question}")
    public Question updateQuestionByQuestion(@RequestBody Question questionElement, @PathVariable String question){
        Question questionToUpdate = questionRepository.findByQuestion(question);
        questionToUpdate.setQuestion(questionElement.getQuestion());
        questionToUpdate.setRightAnswer(questionElement.getRightAnswer());
        questionToUpdate.setWrongAnswerOne(questionElement.getWrongAnswerOne());
        questionToUpdate.setWrongAnswerTwo(questionElement.getWrongAnswerTwo());
        questionToUpdate.setWrongAnswerTwo(questionElement.getWrongAnswerTwo());
        questionToUpdate.setWrongAnswerThree(questionElement.getWrongAnswerThree());
        questionToUpdate.setWrongAnswerFour(questionElement.getWrongAnswerFour());
        return questionRepository.save(questionToUpdate);
    }

    @GetMapping("/get-all-questions/{configuration}")
    public List<Question> getAllQuestions(@CookieValue("token") String tokenCookie, @PathVariable String configuration) {
        System.out.println("try to get all questions for configuration: " +configuration);
        try {
            Algorithm algorithm = Algorithm.HMAC256("test"); //use more secure key
            JWTVerifier verifier = JWT.require(algorithm)
                    .build(); //Reusable verifier instance
            DecodedJWT jwt = verifier.verify(tokenCookie);
            System.out.println("verification successfully! id was: " + jwt.getClaim("id"));

        } catch (JWTVerificationException exception){
            System.out.println("verification not successfully: " + exception);
        }
        return questionRepository.findAllByConfiguration(configuration);
    }
}
