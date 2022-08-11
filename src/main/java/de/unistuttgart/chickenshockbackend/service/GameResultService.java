package de.unistuttgart.chickenshockbackend.service;

import de.unistuttgart.chickenshockbackend.data.*;
import de.unistuttgart.chickenshockbackend.repositories.GameResultRepository;
import de.unistuttgart.chickenshockbackend.repositories.QuestionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@Transactional
public class GameResultService {

    @Autowired
    GameResultRepository gameResultRepository;
    @Autowired
    QuestionRepository questionRepository;

    @Autowired
    AuthorizationService authorizationService;
    /**
     * Cast list of question texts to a List of Questions
     *
     * @param roundResultDTOs list of RoundResults
     * @return a list of questions
     */
    public List<RoundResult> castQuestionList(final List<RoundResultDTO> roundResultDTOs) {
        List<RoundResult> questionList = new ArrayList<>();
        for (RoundResultDTO roundResultDTO : roundResultDTOs) {
            Optional<Question> questionToAdd = questionRepository.findById(roundResultDTO.getQuestionUUId());
            if (questionToAdd.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("There is no question with uuid %s.", roundResultDTO.getQuestionUUId()));
            } else {
                RoundResult roundResult = new RoundResult(questionToAdd.get(),roundResultDTO.getAnswer());
                questionList.add(roundResult);
             }
        }
        return questionList;
    }

    /**
     * Casts a GameResultDTO to GameResult and saves it in the Database
     *
     * @param gameResultDTO extern gameResultDTO
     */
    public void saveGameResult(GameResultDTO gameResultDTO) {
        List<RoundResult> correctQuestions = this.castQuestionList(gameResultDTO.getCorrectAnsweredQuestions());
        List<RoundResult> wrongQuestions = this.castQuestionList(gameResultDTO.getWrongAnsweredQuestions());
        //String playerId = authorizationService.getPlayerId(token);TODO: after login is implemented
        GameResult result = new GameResult(gameResultDTO.getQuestionCount(), gameResultDTO.getTimeLimit(), gameResultDTO.getFinishedInSeconds(), gameResultDTO.getCorrectKillsCount(), gameResultDTO.getWrongKillsCount(), gameResultDTO.getKillsCount(), gameResultDTO.getShotCount(), gameResultDTO.getPoints(), correctQuestions, wrongQuestions, gameResultDTO.getConfigurationAsUUID(), "playerId");
        gameResultRepository.save(result);
    }
}
