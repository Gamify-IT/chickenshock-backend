package de.unistuttgart.moorhuhnbackend.service;

import de.unistuttgart.moorhuhnbackend.data.GameResult;
import de.unistuttgart.moorhuhnbackend.data.GameResultDTO;
import de.unistuttgart.moorhuhnbackend.data.Question;
import de.unistuttgart.moorhuhnbackend.repositories.GameResultRepository;
import de.unistuttgart.moorhuhnbackend.repositories.QuestionRepository;
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
    /**
     * Cast list of question texts to a List of Questions
     *
     * @param questionTextList list of question texts
     * @return a list of questions
     */
    public List<Question> castQuestionList(final List<UUID> questionTextList) {
        List<Question> questionList = new ArrayList<>();
        for (UUID uuid : questionTextList) {
            Optional<Question> questionToAdd = questionRepository.findById(uuid);
            if (questionToAdd.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("There is no question with uuid %s.", uuid));
            } else {
                questionList.add(questionToAdd.get());
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
        List<Question> correctQuestions = this.castQuestionList(gameResultDTO.getCorrectAnsweredQuestions());
        List<Question> wrongQuestions = this.castQuestionList(gameResultDTO.getWrongAnsweredQuestions());
        GameResult result = new GameResult(gameResultDTO.getQuestionCount(), gameResultDTO.getTimeLimit(), gameResultDTO.getFinishedInSeconds(), gameResultDTO.getCorrectKillsCount(), gameResultDTO.getWrongKillsCount(), gameResultDTO.getKillsCount(), gameResultDTO.getShotCount(), gameResultDTO.getPoints(), correctQuestions, wrongQuestions, gameResultDTO.getConfigurationAsUUID(), "");
        gameResultRepository.save(result);
    }
}
