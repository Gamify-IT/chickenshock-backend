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
  QuestionRepository questionRepository;

  @Autowired
  AuthorizationService authorizationService;

  @Autowired
  RoundResultMapper roundResultMapper;

  /**
   * Casts a GameResultDTO to GameResult and saves it in the Database
   *
   * @param gameResultDTO extern gameResultDTO
   */
  public void saveGameResult(final GameResultDTO gameResultDTO) {
    final List<RoundResult> correctQuestions = roundResultMapper.roundResultDTOsToRoundResults(gameResultDTO.getCorrectAnsweredQuestions());
    final List<RoundResult> wrongQuestions = roundResultMapper.roundResultDTOsToRoundResults(gameResultDTO.getWrongAnsweredQuestions());
    //String playerId = authorizationService.getPlayerId(token);TODO: after login is implemented
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
      "playerId"
    );
    gameResultRepository.save(result);

    final int resultScore = calculateResultScore(
      result.getCorrectKillsCount(),
      result.getWrongKillsCount(),
      result.getQuestionCount(),
      result.getTimeLimit()
    );

    final OverworldResultDTO resultDTO = new OverworldResultDTO(
      "CHICKENSHOCK",
      gameResultDTO.getConfigurationAsUUID(),
      resultScore,
      "1"
    );
    resultClient.submit(resultDTO);
  }

  private int calculateResultScore(
    final int correctAnswers,
    final int wrongAnswers,
    final int numberOfQuestions,
    final float time
  ) {
    return (int) (
      (100 * correctAnswers) /
      (correctAnswers + wrongAnswers) *
      Math.min(1, (wrongAnswers + correctAnswers) / Math.min(time / 15, numberOfQuestions))
    );
  }
}
