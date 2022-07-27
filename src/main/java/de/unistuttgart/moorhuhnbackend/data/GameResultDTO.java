package de.unistuttgart.moorhuhnbackend.data;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.lang.Nullable;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

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
    private List<UUID> correctAnsweredQuestions;
    private List<UUID> wrongAnsweredQuestions;
    private UUID configurationAsUUID;

    public GameResultDTO(int questionCount, float timeLimit, float finishedInSeconds, int correctKillsCount, int wrongKillsCount, int killsCount, int shotCount, int points, List<UUID> correctAnsweredQuestions, List<UUID> wrongAnsweredQuestions, UUID configurationAsUUID)
    {
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

        if (id != this.id) return false;
        if (questionCount != this.questionCount) return false;
        if (Float.compare(this.timeLimit, timeLimit) != 0) return false;
        if (Float.compare(this.finishedInSeconds, finishedInSeconds) != 0) return false;
        if (correctKillsCount != this.correctKillsCount) return false;
        if (wrongKillsCount != this.wrongKillsCount) return false;
        if (killsCount != this.killsCount) return false;
        if (shotCount != this.shotCount) return false;
        if (points != this.points) return false;
        if (!correctAnsweredQuestions.equals(this.correctAnsweredQuestions)) return false;
        if (!wrongAnsweredQuestions.equals(this.wrongAnsweredQuestions)) return false;
        return configurationAsUUID.equals(this.configurationAsUUID);
    }
}
