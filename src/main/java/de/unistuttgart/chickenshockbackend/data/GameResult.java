package de.unistuttgart.chickenshockbackend.data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import javax.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

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
