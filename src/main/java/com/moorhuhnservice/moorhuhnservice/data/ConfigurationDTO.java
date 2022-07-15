package com.moorhuhnservice.moorhuhnservice.data;

import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConfigurationDTO {
  String name;
  Set<QuestionDTO> questions;
}
