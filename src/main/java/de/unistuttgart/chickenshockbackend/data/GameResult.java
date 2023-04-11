package de.unistuttgart.chickenshockbackend.data;

import de.unistuttgart.chickenshockbackend.Constants;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.validation.annotation.Validated;

/**
 * The GameResult.class contains all data that is saved after one chickenshock game
 */
@Entity
@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Validated
public class GameResult {

    /**
     * A unique identifier for the game result.
     */
    @Id
    @GeneratedValue(generator = "uuid")
    private UUID id;

    /**
     * The total number of questions that were available.
     */
    @Min(
        value = Constants.MIN_QUESTION_COUNT,
        message = "cannot have less than " + Constants.MIN_QUESTION_COUNT + " questions"
    )
    @Max(
        value = Constants.MAX_QUESTION_COUNT,
        message = "cannot have more than " + Constants.MIN_QUESTION_COUNT + " questions"
    )
    private int questionCount;

    /**
     * The timelimit of the game.
     */
    @Min(value = Constants.MIN_TIME, message = "time has to be ≥ " + Constants.MIN_TIME + "s")
    private float timeLimit;

    /**
     * The time that the user needed to answer all questions.
     */
    @Min(value = Constants.MIN_TIME, message = "cannot finish faster than " + Constants.MIN_TIME + "s")
    private float finishedInSeconds;

    /**
     * The number of questions that were answered correctly.
     */
    @Min(
        value = Constants.MIN_QUESTION_COUNT,
        message = "cannot kill less than " + Constants.MIN_QUESTION_COUNT + " questions"
    )
    @Max(
        value = Constants.MAX_QUESTION_COUNT,
        message = "cannot kill more than " + Constants.MIN_QUESTION_COUNT + " questions"
    )
    private int correctKillsCount;

    /**
     * The number of questions that were answered incorrectly.
     */
    @Min(
        value = Constants.MIN_QUESTION_COUNT,
        message = "cannot kill less than " + Constants.MIN_QUESTION_COUNT + " questions"
    )
    @Max(
        value = Constants.MAX_QUESTION_COUNT,
        message = "cannot kill more than " + Constants.MIN_QUESTION_COUNT + " questions"
    )
    private int wrongKillsCount;

    /**
     * The total number of chickens killed.
     */
    @Min(
        value = Constants.MIN_QUESTION_COUNT,
        message = "cannot kill less than " + Constants.MIN_QUESTION_COUNT + " chickens"
    )
    @Max(
        value = Constants.MAX_QUESTION_COUNT,
        message = "cannot kill more than " + Constants.MIN_QUESTION_COUNT + " chickens"
    )
    private int killsCount;

    /**
     * Number of shots fired.
     */
    private int shotCount;

    /**
     * Points earned.
     */
    @Min(value = Constants.MIN_POINTS, message = "cannot have less than: " + Constants.MIN_POINTS + " points")
    @Max(value = Constants.MAX_POINTS, message = "cannot have more than: " + Constants.MAX_POINTS + " points")
    private int points;

    /**
     * For the correctly answered questions: the text of the question and the selected answer text.
     */
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Valid
    private List<RoundResult> correctAnsweredQuestions;

    /**
     * For the incorrectly answered questions: the text of the question and the selected answer text.
     */
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Valid
    private List<RoundResult> wrongAnsweredQuestions;

    /**
     * UUID of the configuration that was used for this game.
     */
    @NotNull(message = "configurationAsUUID cannot be null")
    private UUID configurationAsUUID;

    /**
     * ID of the player that played this game.
     */
    @NotNull(message = "playerId cannot be null")
    private String playerId;

    /**
     * The date and time when the game was finished.
     */
    @NotNull(message = "playedTime cannot be null")
    private LocalDateTime playedTime;

    public GameResult(
        final int questionCount,
        final float timeLimit,
        final float finishedInSeconds,
        final int correctKillsCount,
        final int wrongKillsCount,
        final int killsCount,
        final int shotCount,
        final int points,
        final List<RoundResult> correctAnsweredQuestions,
        final List<RoundResult> wrongAnsweredQuestions,
        final UUID configurationAsUUID,
        final String playerId
    ) {
        this.questionCount = questionCount;
        this.timeLimit = timeLimit;
        this.finishedInSeconds = finishedInSeconds;
        this.correctKillsCount = correctKillsCount;
        this.wrongKillsCount = wrongKillsCount;
        this.killsCount = killsCount;
        this.shotCount = shotCount;
        this.points = points;
        this.correctAnsweredQuestions = correctAnsweredQuestions;
        this.wrongAnsweredQuestions = wrongAnsweredQuestions;
        this.configurationAsUUID = configurationAsUUID;
        this.playerId = playerId;
        this.playedTime = LocalDateTime.now();
    }
}
