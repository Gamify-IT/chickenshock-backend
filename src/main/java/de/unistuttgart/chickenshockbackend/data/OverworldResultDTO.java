package de.unistuttgart.chickenshockbackend.data;

import de.unistuttgart.chickenshockbackend.Constants;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.UUID;

/**
 * The OverworldResultDTO.class contains all the info that is sent to the Overworld-backend as the result of a game.
 * This is then used to calculate the player's progress.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Validated
public class OverworldResultDTO {

    /**
     * The name of the minigame. In this case: "CHICKENSHOCK".
     */
    @NotNull(message = "game cannot be null")
    String game;

    /**
     * A unique identifier for the game configuration used for the game.
     */
    @NotNull(message = "configurationId cannot be null")
    UUID configurationId;

    /**
     * The score that the player achieved in the game.
     */
    @Min(value = Constants.MIN_SCORE, message = "Score cannot be less than " + Constants.MIN_SCORE)
    @Max(value = Constants.MAX_SCORE, message = "Score cannot be higher than " + Constants.MAX_SCORE)
    long score;

    /**
     * The unique identifier for the user.
     */
    @NotNull(message = "user cannot be null")
    @NotBlank(message = "user cannot be blank")
    String userId;
}
