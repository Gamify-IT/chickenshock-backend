package de.unistuttgart.chickenshockbackend.data;

import java.util.Objects;
import java.util.UUID;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.lang.Nullable;
import org.springframework.validation.annotation.Validated;

/**
 * The RoundResultDTO.class contains the round result related information.
 * A round represents a single question and the answer given by the user.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Validated
public class RoundResultDTO {

    /**
     * A unique identifier for the round result.
     */
    @Nullable
    UUID id;

    /**
     * The question text.
     */
    @NotNull(message = "questionUUId cannot be null")
    UUID questionUUId;

    /**
     * The text of the answer chosen by the user.
     */
    @NotNull(message = "answer cannot be null")
    @NotBlank(message = "answer cannot be blank")
    String answer;

    public RoundResultDTO(final UUID questionUUId, final String answer) {
        this.questionUUId = questionUUId;
        this.answer = answer;
    }

    public boolean equalsContent(final RoundResultDTO other) {
        if (this == other) return true;
        if (other == null) return false;
        return (Objects.equals(questionUUId, other.questionUUId) && Objects.equals(answer, other.answer));
    }
}
