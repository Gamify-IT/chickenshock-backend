package com.moorhuhnservice.moorhuhnservice.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.moorhuhnservice.moorhuhnservice.BaseClasses.Question;
import com.moorhuhnservice.moorhuhnservice.repositories.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.slf4j.*;

@RestController
public class MoorhuhnController {

    private static final Logger logger = LoggerFactory.getLogger(MoorhuhnController.class);

    @Autowired
    QuestionRepository questionRepository;

    @PostMapping("/save-all-questions")
    public List<Question> saveAllQuestions(@RequestBody List<Question> questions) {
        logger.debug("try to save all "+questions.size()+" questions: ");
        List<Question> addedQuestions = new ArrayList<>();
        for (Question question:questions) {
            addedQuestions.add(questionRepository.save(new Question(question.getConfiguration(),question.getQuestionText(),question.getRightAnswer(),question.getWrongAnswerOne(),question.getWrongAnswerTwo(),question.getWrongAnswerThree(),question.getWrongAnswerFour())));
        }
        return addedQuestions;
    }

    @PostMapping("/save-a-question")
    public Question saveFirstTestQuestion(@RequestBody Question question) {
        logger.debug("try to save a question");
        Question questionTest = new Question(question.getConfiguration(), question.getQuestionText(), question.getRightAnswer(), question.getWrongAnswerOne(), question.getWrongAnswerTwo(), question.getWrongAnswerThree(), question.getWrongAnswerFour());
        questionRepository.save(questionTest);
        return questionTest;
    }

    @DeleteMapping("/delete-question-element-by-id/{id}")
    public Question deleteQuestionByQuestion(@PathVariable long id){
        Optional<Question> questionToDelete = questionRepository.findById(id);
        if(questionToDelete.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "There is no question with id"+ id);
        }else{
            questionRepository.deleteById(id);
            return questionToDelete.get();
        }
    }

    @PutMapping("/put-question-element-by-id/{id}")
    public Question updateQuestionByQuestion(@RequestBody Question questionElement, @PathVariable long id){
        Optional<Question> questionToUpdate = questionRepository.findById(id);
        if(questionToUpdate.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "There is no question with id"+ id);
        }else{
            questionToUpdate.get().setQuestion(questionElement.getQuestionText());
            questionToUpdate.get().setRightAnswer(questionElement.getRightAnswer());
            questionToUpdate.get().setWrongAnswerOne(questionElement.getWrongAnswerOne());
            questionToUpdate.get().setWrongAnswerTwo(questionElement.getWrongAnswerTwo());
            questionToUpdate.get().setWrongAnswerTwo(questionElement.getWrongAnswerTwo());
            questionToUpdate.get().setWrongAnswerThree(questionElement.getWrongAnswerThree());
            questionToUpdate.get().setWrongAnswerFour(questionElement.getWrongAnswerFour());
            return questionRepository.save(questionToUpdate.get());
        }
    }

    @GetMapping("/get-all-questions/{configuration}")
    public List<Question> getAllQuestions(@CookieValue("token") String tokenCookie, @PathVariable String configuration) {
        logger.debug("try to get all questions for configuration: " +configuration);
        try {
            Algorithm algorithm = Algorithm.HMAC256("test"); //use more secure key
            JWTVerifier verifier = JWT.require(algorithm)
                    .build(); //Reusable verifier instance
            DecodedJWT jwt = verifier.verify(tokenCookie);
            logger.debug("verification successfully! id was: " + jwt.getClaim("id"));

        } catch (JWTVerificationException exception){
            logger.debug("verification not successfully: " + exception);
        }
        return questionRepository.findAllByConfiguration(configuration);
    }

}
