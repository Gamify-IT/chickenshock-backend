package de.unistuttgart.chickenshockbackend.data;

import de.unistuttgart.chickenshockbackend.Constants;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.validation.annotation.Validated;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.Set;
import java.util.UUID;

/**
 * The Configuration.class contains all data that has to be stored to configure a chickenshock game
 */
@Entity
@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Validated
public class Configuration {

    @Id
    @GeneratedValue(generator = "uuid")
    UUID id;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @Valid
    Set<Question> questions;

    @Min(value = Constants.MIN_TIME, message = "time has to be ≥ " + Constants.MIN_TIME + "s")
    int time;

    public Configuration(final Set<Question> questions, final int time) {
        this.questions = questions;
        this.time = time;
    }

    public void addQuestion(final Question question) {
        this.questions.add(question);
    }

    public void removeQuestion(final Question question) {
        this.questions.remove(question);
    }
}
