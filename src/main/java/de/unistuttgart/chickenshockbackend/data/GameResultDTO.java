package de.unistuttgart.chickenshockbackend.data;

import de.unistuttgart.chickenshockbackend.Constants;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.lang.Nullable;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

/**
 * The GameResultDTO.class contains all data that is saved after one chickenshock game
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Validated
public class GameResultDTO {

    @Nullable
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
    private float timeLimit;

    @Min(value = Constants.MIN_TIME, message = "cannot finish faster than " + Constants.MIN_TIME + "s")
    private float finishedInSeconds;

    @Min(
        value = Constants.MIN_QUESTION_COUNT,
        message = "cannot kill less than " + Constants.MIN_QUESTION_COUNT + " chicken"
    )
    @Max(
        value = Constants.MAX_QUESTION_COUNT,
        message = "cannot kill more than " + Constants.MIN_QUESTION_COUNT + " chicken"
    )
    private int correctKillsCount;

    @Min(
        value = Constants.MIN_QUESTION_COUNT,
        message = "cannot kill less than " + Constants.MIN_QUESTION_COUNT + " chickens"
    )
    @Max(
        value = Constants.MAX_QUESTION_COUNT,
        message = "cannot kill more than " + Constants.MIN_QUESTION_COUNT + " chickens"
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

    @Min(value = Constants.MIN_POINTS, message = "cannot have less than " + Constants.MIN_POINTS + " points")
    @Max(value = Constants.MAX_POINTS, message = "cannot have more than " + Constants.MAX_POINTS + " points")
    private int points;

    @Valid
    private List<RoundResultDTO> correctAnsweredQuestions;

    @Valid
    private List<RoundResultDTO> wrongAnsweredQuestions;

    @NotNull(message = "configurationAsUUID cannot be null")
    private UUID configurationAsUUID;

    public GameResultDTO(
        final int questionCount,
        final float timeLimit,
        final float finishedInSeconds,
        final int correctKillsCount,
        final int wrongKillsCount,
        final int killsCount,
        final int shotCount,
        final int points,
        final List<RoundResultDTO> correctAnsweredQuestions,
        final List<RoundResultDTO> wrongAnsweredQuestions,
        final UUID configurationAsUUID
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
    }

    public boolean equalsContent(final GameResultDTO other) {
        if (this == other) return true;
        if (other == null) return false;

        if (id != other.id) return false;
        if (questionCount != other.questionCount) return false;
        if (Float.compare(other.timeLimit, timeLimit) != 0) return false;
        if (Float.compare(other.finishedInSeconds, finishedInSeconds) != 0) return false;
        if (correctKillsCount != other.correctKillsCount) return false;
        if (wrongKillsCount != other.wrongKillsCount) return false;
        if (killsCount != other.killsCount) return false;
        if (shotCount != other.shotCount) return false;
        if (points != other.points) return false;
        if (!correctAnsweredQuestions.equals(other.correctAnsweredQuestions)) return false;
        if (!wrongAnsweredQuestions.equals(other.wrongAnsweredQuestions)) return false;
        return configurationAsUUID.equals(other.configurationAsUUID);
    }
}
