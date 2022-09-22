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

    @Id
    @GeneratedValue(generator = "uuid")
    private UUID id;

    @Min(
        value = Constants.MIN_QUESTION_COUNT,
        message = "cannot have less than " + Constants.MIN_QUESTION_COUNT + " questions"
    )
    @Max(
        value = Constants.MAX_QUESTION_COUNT,
        message = "cannot have more than " + Constants.MIN_QUESTION_COUNT + " questions"
    )
    private int questionCount;

    @Min(value = Constants.MIN_TIME, message = "time has to be ≥ " + Constants.MIN_TIME + "s")
    @Max(value = Constants.MAX_TIME, message = "time has to be ≤ " + Constants.MAX_TIME + "s")
    private float timeLimit;

    @Min(value = Constants.MIN_TIME, message = "cannot finish faster than " + Constants.MIN_TIME + "s")
    @Max(value = Constants.MAX_TIME, message = "cannot take longer than " + Constants.MAX_TIME + "s")
    private float finishedInSeconds;

    @Min(
        value = Constants.MIN_QUESTION_COUNT,
        message = "cannot kill less than " + Constants.MIN_QUESTION_COUNT + " questions"
    )
    @Max(
        value = Constants.MAX_QUESTION_COUNT,
        message = "cannot kill more than " + Constants.MIN_QUESTION_COUNT + " questions"
    )
    private int correctKillsCount;

    @Min(
        value = Constants.MIN_QUESTION_COUNT,
        message = "cannot kill less than " + Constants.MIN_QUESTION_COUNT + " questions"
    )
    @Max(
        value = Constants.MAX_QUESTION_COUNT,
        message = "cannot kill more than " + Constants.MIN_QUESTION_COUNT + " questions"
    )
    private int wrongKillsCount;

    @Min(
        value = Constants.MIN_QUESTION_COUNT,
        message = "cannot kill less than " + Constants.MIN_QUESTION_COUNT + " chickens"
    )
    @Max(
        value = Constants.MAX_QUESTION_COUNT,
        message = "cannot kill more than " + Constants.MIN_QUESTION_COUNT + " chickens"
    )
    private int killsCount;

    private int shotCount;

    @Min(value = Constants.MIN_POINTS, message = "cannot have less than: " + Constants.MIN_POINTS + " points")
    @Max(value = Constants.MAX_POINTS, message = "cannot have more than: " + Constants.MAX_POINTS + " points")
    private int points;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Valid
    private List<RoundResult> correctAnsweredQuestions;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Valid
    private List<RoundResult> wrongAnsweredQuestions;

    @NotNull(message = "configurationAsUUID cannot be null")
    private UUID configurationAsUUID;

    @NotNull(message = "playerId cannot be null")
    private String playerId;

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
