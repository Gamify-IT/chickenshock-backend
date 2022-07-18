package com.moorhuhnservice.moorhuhnservice.data;

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

  public ConfigurationDTO(Set<QuestionDTO> questions) {
    this.questions = questions;
  }

  public boolean equalsContent(final Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    final ConfigurationDTO that = (ConfigurationDTO) o;
    return Objects.equals(questions, that.questions);
  }
}
