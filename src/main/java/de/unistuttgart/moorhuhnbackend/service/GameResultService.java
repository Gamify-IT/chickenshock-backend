package de.unistuttgart.moorhuhnbackend.service;

import de.unistuttgart.moorhuhnbackend.data.GameResult;
import de.unistuttgart.moorhuhnbackend.data.GameResultDTO;
import de.unistuttgart.moorhuhnbackend.data.Question;
import de.unistuttgart.moorhuhnbackend.repositories.ConfigurationRepository;
import de.unistuttgart.moorhuhnbackend.repositories.GameResultRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@Transactional
public class GameResultService {

    @Autowired
    GameResultRepository gameResultRepository;

    @Autowired
    ConfigurationRepository configurationRepository;
    /**
     * Cast list of question texts to a List of Questions
     *
     * @param questionTextList list of question texts
     * @return a list of questions
     */
    public List<Question> castQuestionList(final List<String> questionTextList) {
        List<Question> questionList = new ArrayList<>();
        for (String questionText : questionTextList) {
            Question questionToAdd = configurationRepository.findByText(questionText);
            if (questionToAdd == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("There is no question with questionText %s.", questionText));
            } else {
                questionList.add(questionToAdd);
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
