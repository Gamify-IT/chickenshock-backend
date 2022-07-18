package com.moorhuhnservice.moorhuhnservice.data.mapper;

import com.moorhuhnservice.moorhuhnservice.data.Configuration;
import com.moorhuhnservice.moorhuhnservice.data.ConfigurationDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ConfigurationMapper {
  ConfigurationDTO configurationToConfigurationDTO(final Configuration configuration);

  Configuration configurationDTOToConfiguration(final ConfigurationDTO configurationDTO);
}
