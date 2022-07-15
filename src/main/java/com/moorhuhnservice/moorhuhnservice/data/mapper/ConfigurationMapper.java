package com.moorhuhnservice.moorhuhnservice.data.mapper;

import com.moorhuhnservice.moorhuhnservice.data.Configuration;
import com.moorhuhnservice.moorhuhnservice.data.ConfigurationDTO;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ConfigurationMapper {
  ConfigurationDTO configurationToConfigurationDTO(Configuration configuration);

  Configuration configurationDTOToConfiguration(ConfigurationDTO configurationDTO);
}
