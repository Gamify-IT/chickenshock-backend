package de.unistuttgart.moorhuhnbackend.data;

import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConfigurationDTO {

  @Nullable
  UUID id;

  Set<QuestionDTO> questions;

  public ConfigurationDTO(final Set<QuestionDTO> questions) {
    this.questions = questions;
  }

  public boolean equalsContent(final ConfigurationDTO other) {
    if (this == other) return true;
    if (other == null) return false;
    return Objects.equals(questions, other.questions);
  }
}
