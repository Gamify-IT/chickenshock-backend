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
import javax.validation.constraints.Min;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

/**
 * The ConfigurationDTO.class contains all data that has to be stored to configure a chickenshock game
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Validated
public class ConfigurationDTO {

    /**
     * A unique identifier for the configuration.
     */
    @Nullable
    UUID id;

    /**
     * The questions that are part of the configuration.
     */
    @Valid
    Set<QuestionDTO> questions;

    /**
     * The time that is available for the game.
     */
    @Min(value = Constants.MIN_TIME, message = "time has to be ≥ " + Constants.MIN_TIME + "s")
    int time;

    public ConfigurationDTO(final Set<QuestionDTO> questions, final int time) {
        this.questions = questions;
        this.time = time;
    }

    public boolean equalsContent(final ConfigurationDTO other) {
        if (this == other) return true;
        if (other == null) return false;
        return Objects.equals(questions, other.questions) && Objects.equals(time, other.time);
    }
}
