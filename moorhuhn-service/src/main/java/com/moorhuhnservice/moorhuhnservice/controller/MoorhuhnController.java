package com.moorhuhnservice.moorhuhnservice.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.moorhuhnservice.moorhuhnservice.data.Question;
import com.moorhuhnservice.moorhuhnservice.data.QuestionDTO;
import com.moorhuhnservice.moorhuhnservice.data.QuestionMapper;
import com.moorhuhnservice.moorhuhnservice.repositories.QuestionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.*;

@RestController("api/v1/minigames/moorhuhn")
@Slf4j
public class MoorhuhnController {
    final QuestionRepository questionRepository;
    final QuestionMapper questionMapper;

    public MoorhuhnController(QuestionRepository questionRepository, QuestionMapper questionMapper) {
        this.questionRepository = questionRepository;
        this.questionMapper = questionMapper;
    }

    @PostMapping("/save-all-questions")
    public List<Question> saveAllQuestions(@RequestBody List<QuestionDTO> listOfQuestionDTOs) {
        log.debug("try to save all {} listOfQuestionDTOs: ", listOfQuestionDTOs.size());
        // Should be moved into a service
        List<Question> addedQuestions = new ArrayList<>();
        for (QuestionDTO questionDTO : listOfQuestionDTOs) {
            Question question = questionMapper.questionDTOToQuestion(questionDTO);
            Question result = questionRepository.save(question);
            addedQuestions.add(result);
        }
        return addedQuestions;
    }

    @PostMapping("/save-a-question")
    public Question saveFirstTestQuestion(@RequestBody Question question) {
        log.debug("try to save a question");
        Question questionTest = new Question(question.getConfiguration(), question.getQuestionText(), question.getRightAnswer(), question.getWrongAnswerOne(), question.getWrongAnswerTwo(), question.getWrongAnswerThree(), question.getWrongAnswerFour());
        questionRepository.save(questionTest);
        return questionTest;
    }

    @DeleteMapping("/delete-question-element-by-id/{id}")
    public Question deleteQuestionByQuestion(@PathVariable long id) {
        Optional<Question> questionToDelete = questionRepository.findById(id);
        if (questionToDelete.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "There is no question with id" + id);
        } else {
            questionRepository.deleteById(id);
            return questionToDelete.get();
        }
    }

    @PutMapping("/put-question-element-by-id/{id}")
    public Question updateQuestionByQuestion(@RequestBody Question questionElement, @PathVariable long id) {
        Optional<Question> questionToUpdate = questionRepository.findById(id);
        if (questionToUpdate.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "There is no question with id" + id);
        } else {
            questionToUpdate.get().setQuestionText(questionElement.getQuestionText());
            questionToUpdate.get().setRightAnswer(questionElement.getRightAnswer());
            questionToUpdate.get().setWrongAnswerOne(questionElement.getWrongAnswerOne());
            questionToUpdate.get().setWrongAnswerTwo(questionElement.getWrongAnswerTwo());
            questionToUpdate.get().setWrongAnswerTwo(questionElement.getWrongAnswerTwo());
            questionToUpdate.get().setWrongAnswerThree(questionElement.getWrongAnswerThree());
            questionToUpdate.get().setWrongAnswerFour(questionElement.getWrongAnswerFour());
            return questionRepository.save(questionToUpdate.get());
        }
    }

    @GetMapping("/get-all-questions/{configurationId}")
    public List<Question> getAllQuestions(@CookieValue("token") String tokenCookie, @PathVariable String configurationId) {
        log.debug("try to get all questions for configuration: {}", configurationId);
        try {
            Algorithm algorithm = Algorithm.HMAC256("test"); //use more secure key
            JWTVerifier verifier = JWT.require(algorithm)
                    .build(); //Reusable verifier instance
            DecodedJWT jwt = verifier.verify(tokenCookie);
            log.debug("verification successfully! id was: {}", jwt.getClaim("id"));

        } catch (JWTVerificationException exception) {
            log.debug("verification not successfully.", exception);
        }
        return questionRepository.findAllByConfiguration(configurationId);
    }

}
