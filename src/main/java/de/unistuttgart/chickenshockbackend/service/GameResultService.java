package de.unistuttgart.chickenshockbackend.service;

import de.unistuttgart.chickenshockbackend.clients.ResultClient;
import de.unistuttgart.chickenshockbackend.data.*;
import de.unistuttgart.chickenshockbackend.data.mapper.RoundResultMapper;
import de.unistuttgart.chickenshockbackend.repositories.GameResultRepository;
import de.unistuttgart.chickenshockbackend.repositories.QuestionRepository;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@Transactional
public class GameResultService {

  @Autowired
  ResultClient resultClient;

  @Autowired
  GameResultRepository gameResultRepository;

  @Autowired
  RoundResultMapper roundResultMapper;

  /**
   * Casts a GameResultDTO to GameResult and saves it in the Database
   *
   * @param gameResultDTO extern gameResultDTO
   */
  public void saveGameResult(final GameResultDTO gameResultDTO, String userId) {
    final List<RoundResult> correctQuestions = roundResultMapper.roundResultDTOsToRoundResults(
      gameResultDTO.getCorrectAnsweredQuestions()
    );
    final List<RoundResult> wrongQuestions = roundResultMapper.roundResultDTOsToRoundResults(
      gameResultDTO.getWrongAnsweredQuestions()
    );
    final GameResult result = new GameResult(
      gameResultDTO.getQuestionCount(),
      gameResultDTO.getTimeLimit(),
      gameResultDTO.getFinishedInSeconds(),
      gameResultDTO.getCorrectKillsCount(),
      gameResultDTO.getWrongKillsCount(),
      gameResultDTO.getKillsCount(),
      gameResultDTO.getShotCount(),
      gameResultDTO.getPoints(),
      correctQuestions,
      wrongQuestions,
      gameResultDTO.getConfigurationAsUUID(),
      userId
    );
    gameResultRepository.save(result);

    final int resultScore = calculateResultScore(result.getCorrectKillsCount(), result.getQuestionCount());
    System.out.println(result.getTimeLimit());

    final OverworldResultDTO resultDTO = new OverworldResultDTO(
      "CHICKENSHOCK",
      gameResultDTO.getConfigurationAsUUID(),
      resultScore,
      userId
    );
    resultClient.submit(resultDTO);
  }

  private int calculateResultScore(final int correctAnswers, final int numberOfQuestions) {
    return (int) ((100.0 * correctAnswers) / numberOfQuestions);
  }
}
