package de.unistuttgart.chickenshockbackend.service;

import de.unistuttgart.chickenshockbackend.clients.ResultClient;
import de.unistuttgart.chickenshockbackend.data.GameResult;
import de.unistuttgart.chickenshockbackend.data.GameResultDTO;
import de.unistuttgart.chickenshockbackend.data.OverworldResultDTO;
import de.unistuttgart.chickenshockbackend.data.RoundResult;
import de.unistuttgart.chickenshockbackend.data.mapper.RoundResultMapper;
import de.unistuttgart.chickenshockbackend.repositories.GameResultRepository;
import feign.FeignException;
import java.util.List;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

/**
 * This service handles the logic for the GameResultController.class
 */
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

    private static int hundredScoreCount = 0;


    /**
     * Casts a GameResultDTO to GameResult and saves it in the Database
     *
     * @param gameResultDTO extern gameResultDTO
     * @param userId id of the user
     * @param accessToken accessToken of the user
     * @throws IllegalArgumentException if at least one of the arguments is null
     */
    public void saveGameResult(
        final @Valid GameResultDTO gameResultDTO,
        final String userId,
        final String accessToken
    ) {
        if (gameResultDTO == null || userId == null || accessToken == null) {
            throw new IllegalArgumentException("gameResultDTO or userId or accessToken is null");
        }
        final OverworldResultDTO resultDTO = createOverworldResult(gameResultDTO, userId);
        try {
            resultClient.submit(accessToken, resultDTO);
            final List<RoundResult> correctQuestions = roundResultMapper.roundResultDTOsToRoundResults(
                gameResultDTO.getCorrectAnsweredQuestions()
            );
            final List<RoundResult> wrongQuestions = roundResultMapper.roundResultDTOsToRoundResults(
                gameResultDTO.getWrongAnsweredQuestions()
            );

            final int score = calculateResultScore(gameResultDTO.getCorrectKillsCount(),gameResultDTO.getQuestionCount());
            final int rewards = calculateRewards(score);
            final GameResult result = new @Valid GameResult(
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
                userId,
                    score,
                    rewards
            );
            gameResultDTO.setScore(score);
            gameResultDTO.setRewards(rewards);

            gameResultRepository.save(result);
        } catch (final FeignException.BadGateway badGateway) {
            final String warning =
                "The Overworld backend is currently not available. The result was NOT saved. Please try again later";
            log.error(warning + badGateway);
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, warning);
        } catch (final FeignException.NotFound notFound) {
            final String warning = "The result could not be saved. Unknown User";
            log.error(warning + notFound);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, warning);
        }
    }

    /**
     * Create an OverworldResultDTO
     *
     * @param gameResultDTO contains all game related data
     * @param userId        id of the player
     * @return OverworldResultDTO
     */
    private OverworldResultDTO createOverworldResult(final @Valid GameResultDTO gameResultDTO, final String userId) {
        final int resultScore = calculateResultScore(
            gameResultDTO.getCorrectKillsCount(),
            gameResultDTO.getQuestionCount()
        );
        final int rewards = calculateRewards(resultScore);
        return new @Valid OverworldResultDTO(
            "CHICKENSHOCK",
            gameResultDTO.getConfigurationAsUUID(),
            resultScore,
            userId,
                rewards

        );
    }

    /**
     * calculates the score a player made
     *
     * @param correctAnswers    correct answer count
     * @param numberOfQuestions available question count
     * @return score as int in %
     * @throws IllegalArgumentException if correctAnswers < 0 || numberOfQuestions < correctAnswers
     */
    private int calculateResultScore(final int correctAnswers, final int numberOfQuestions) {
        if (correctAnswers < 0 || numberOfQuestions < correctAnswers) {
            throw new IllegalArgumentException(
                String.format(
                    "correctAnswers (%s) or numberOfQuestions (%s) is not possible",
                    correctAnswers,
                    numberOfQuestions
                )
            );
        }
        return (int) ((100.0 * correctAnswers) / numberOfQuestions);
    }

    /**
     * This method calculates the rewards for one chickenshock round based on the gained scores in the
     * current round
     *
     * first three rounds: 10 coins, after that for each 100% only 5 coins
     * if score != 100% then the score is divided by 10
     *
     * @param resultScore the score gained in the game
     * @return rewards as int
     * @throws IllegalArgumentException if resultScore < 0
     */
    private int calculateRewards(final int resultScore) {
        if (resultScore < 0) {
            throw new IllegalArgumentException("Result score cannot be less than zero");
        }
        if (resultScore == 100 && hundredScoreCount < 3) {
            hundredScoreCount++;
            return 10;
        } else if (resultScore == 100 && hundredScoreCount >= 3) {
            return 5;
        }
        return resultScore/10;
    }
}
