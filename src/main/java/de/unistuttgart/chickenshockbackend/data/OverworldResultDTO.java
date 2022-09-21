package de.unistuttgart.chickenshockbackend.data;

import de.unistuttgart.chickenshockbackend.Constants;
import java.util.UUID;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.validation.annotation.Validated;

/**
 * The OverworldResultDTO.class contains all the info that is sent to the Overworld-backend
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Validated
public class OverworldResultDTO {

    @NotNull(message = "game cannot be null")
    String game;

    @NotNull(message = "configurationId cannot be null")
    UUID configurationId;

    @Min(Constants.MIN_SCORE)
    @Max(Constants.MAX_SCORE)
    long score;

    @NotNull(message = "user cannot be null")
    @NotBlank(message = "user cannot be blank")
    String userId;
}
