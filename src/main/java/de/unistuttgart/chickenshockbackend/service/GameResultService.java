package de.unistuttgart.chickenshockbackend.service;

import de.unistuttgart.chickenshockbackend.clients.ResultClient;
import de.unistuttgart.chickenshockbackend.data.*;
import de.unistuttgart.chickenshockbackend.data.mapper.RoundResultMapper;
import de.unistuttgart.chickenshockbackend.repositories.GameResultRepository;
import de.unistuttgart.chickenshockbackend.repositories.QuestionRepository;
import java.util.List;

import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

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
    final int resultScore = calculateResultScore(gameResultDTO.getCorrectKillsCount(), gameResultDTO.getQuestionCount());
    final OverworldResultDTO resultDTO = new OverworldResultDTO(
            "CHICKENSHOCK",
            gameResultDTO.getConfigurationAsUUID(),
            resultScore,
            "1"
    );
    try {
      resultClient.submit(resultDTO);
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
              "playerId"
      );
      gameResultRepository.save(result);
    } catch (FeignException.BadGateway badGateway) {
      String warning = "The Overworld backend is currently not available. The result was NOT saved. Please try again later";
      log.warn(warning + badGateway);
      throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE,
              warning);
    } catch (FeignException.NotFound notFound) {
      String warning = "The result could not be saved. Unknown User";
      log.warn(warning + notFound);
      throw new ResponseStatusException(HttpStatus.NOT_FOUND,
              warning);
    }
  }

  private int calculateResultScore(final int correctAnswers, final int numberOfQuestions) {
    return (int) ((100.0 * correctAnswers) / numberOfQuestions);
  }
}
