package de.unistuttgart.chickenshockbackend.data;

import java.util.List;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.lang.Nullable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GameResultDTO {

    @Nullable
    private long id;

    private int questionCount;
    private float timeLimit;
    private float finishedInSeconds;
    private int correctKillsCount;
    private int wrongKillsCount;
    private int killsCount;
    private int shotCount;
    private int points;
    private List<RoundResultDTO> correctAnsweredQuestions;
    private List<RoundResultDTO> wrongAnsweredQuestions;
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
