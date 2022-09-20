package de.unistuttgart.chickenshockbackend.data;

import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * The OverworldResultDTO.class contains all the info that is sent to the Overworld-backend
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OverworldResultDTO {

    String game;
    UUID configurationId;
    long score;
    String userId;
}
