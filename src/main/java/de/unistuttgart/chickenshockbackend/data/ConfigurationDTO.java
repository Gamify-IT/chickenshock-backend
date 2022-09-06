package de.unistuttgart.chickenshockbackend.data;

import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.lang.Nullable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ConfigurationDTO {

  @Nullable
  UUID id;

  Set<QuestionDTO> questions;
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
