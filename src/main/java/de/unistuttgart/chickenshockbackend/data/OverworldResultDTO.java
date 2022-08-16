package de.unistuttgart.chickenshockbackend.data;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OverworldResultDTO {

  String game;
  UUID configurationId;
  long score;
  String userId;
}
