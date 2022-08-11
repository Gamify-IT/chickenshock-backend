package de.unistuttgart.chickenshockbackend.data;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GameResult {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private int questionCount;
    private float timeLimit;
    private float finishedInSeconds;
    private int correctKillsCount;
    private int wrongKillsCount;
    private int killsCount;
    private int shotCount;
    private int points;
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<RoundResult> correctAnsweredQuestions;
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<RoundResult> wrongAnsweredQuestions;
    private UUID configurationAsUUID;
    private String playerId;
    private LocalDateTime playedTime;

    public GameResult(int questionCount, float timeLimit, float finishedInSeconds, int correctKillsCount, int wrongKillsCount, int killsCount, int shotCount, int points, List<RoundResult> correctAnsweredQuestions, List<RoundResult> wrongAnsweredQuestions, UUID configurationAsUUID, String playerId)
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
        this.playerId = playerId;
        this.playedTime = LocalDateTime.now();
    }
}
