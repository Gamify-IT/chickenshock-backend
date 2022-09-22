package de.unistuttgart.chickenshockbackend.data;

import java.util.UUID;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.validation.annotation.Validated;

/**
 * The RoundResult.class contains the round result related information.
 * A round represents a single question and the answer given by the user.
 */
@Entity
@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Validated
public class RoundResult {

    /**
     * A unique identifier for the round result.
     */
    @Id
    @GeneratedValue(generator = "uuid")
    UUID id;

    /**
     * The question text.
     */
    @NotNull(message = "question cannot be null")
    @ManyToOne
    @Valid
    Question question;

    /**
     * The text of the answer chosen by the user.
     */
    @NotNull(message = "answer cannot be null")
    @NotBlank(message = "answer cannot be blank")
    String answer;

    public RoundResult(final Question question, final String answer) {
        this.question = question;
        this.answer = answer;
    }
}
