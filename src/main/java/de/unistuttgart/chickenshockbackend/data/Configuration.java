package de.unistuttgart.chickenshockbackend.data;

import de.unistuttgart.chickenshockbackend.Constants;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.validation.annotation.Validated;

/**
 * The Configuration.class contains all data that has to be stored to configure a chickenshock game
 */
@Entity
@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Validated
public class Configuration {

    /**
     * A unique identifier for the configuration.
     */
    @Id
    @GeneratedValue(generator = "uuid")
    UUID id;

    /**
     * The questions that are part of the configuration.
     */
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @Valid
    Set<Question> questions;

    /**
     * The time that is available for the game.
     */
    @Min(value = Constants.MIN_TIME, message = "time has to be ≥ " + Constants.MIN_TIME + "s")
    int time;

    public Configuration(final Set<Question> questions, final int time) {
        this.questions = questions;
        this.time = time;
    }

    /**
     * Add a question to the configuration.
     * @param question the question to add
     */
    public void addQuestion(final Question question) {
        this.questions.add(question);
    }

    /**
     * Remove a question from the configuration.
     * @param question the question to remove
     */
    public void removeQuestion(final Question question) {
        this.questions.remove(question);
    }

    @Override
    public Configuration clone() {
        return new Configuration(this.questions.stream().map(Question::clone).collect(Collectors.toSet()), this.time);
    }
}
