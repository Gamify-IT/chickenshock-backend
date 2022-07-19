package com.moorhuhnservice.moorhuhnservice.controller;

import com.moorhuhnservice.moorhuhnservice.data.*;
import com.moorhuhnservice.moorhuhnservice.data.mapper.ConfigurationMapper;
import com.moorhuhnservice.moorhuhnservice.data.mapper.QuestionMapper;
import com.moorhuhnservice.moorhuhnservice.repositories.ConfigurationRepository;
import com.moorhuhnservice.moorhuhnservice.service.ConfigService;
import java.util.List;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/minigames/moorhuhn/configurations")
@Slf4j
public class ConfigController {

  @Autowired
  ConfigurationRepository configurationRepository;

  @Autowired
  ConfigService configService;

  @Autowired
  QuestionMapper questionMapper;

  @Autowired
  ConfigurationMapper configurationMapper;

  @GetMapping("")
  public List<ConfigurationDTO> getConfigurations() {
    log.debug("get all configurations");
    List<Configuration> configurations = configurationRepository.findAll();
    List<ConfigurationDTO> configurationDTOs = configurationMapper.configurationsToConfigurationDTOs(configurations);
    return configurationDTOs;
  }

  @GetMapping("/{id}")
  public ConfigurationDTO getConfiguration(@PathVariable final UUID id) {
    log.debug("get configuration {}", id);
    final Configuration configuration = configService.getConfiguration(id);
    return configurationMapper.configurationToConfigurationDTO(configuration);
  }

  @PostMapping("")
  @ResponseStatus(HttpStatus.CREATED)
  public ConfigurationDTO createConfiguration(@RequestBody final ConfigurationDTO configurationDTO) {
    log.debug("create configuration {}", configurationDTO);
    return configService.saveConfiguration(configurationDTO);
  }

  @PutMapping("/{id}")
  public ConfigurationDTO updateConfiguration(
    @PathVariable final UUID id,
    @RequestBody final ConfigurationDTO configurationDTO
  ) {
    log.debug("update configuration {} with {}", id, configurationDTO);
    return configService.updateConfiguration(id, configurationDTO);
  }

  @DeleteMapping("/{id}")
  public ConfigurationDTO deleteConfiguration(@PathVariable final UUID id) {
    log.debug("delete configuration {}", id);
    return configService.deleteConfiguration(id);
  }

  @PostMapping("/{id}/questions")
  @ResponseStatus(HttpStatus.CREATED)
  public QuestionDTO addQuestionToConfiguration(
    @PathVariable final UUID id,
    @RequestBody final QuestionDTO questionDTO
  ) {
    log.debug("add question {} to configuration {}", questionDTO, id);
    return configService.addQuestionToConfiguration(id, questionDTO);
  }

  @DeleteMapping("/{id}/questions/{questionId}")
  public QuestionDTO removeQuestionFromConfiguration(@PathVariable final UUID id, @PathVariable final UUID questionId) {
    log.debug("remove question {} from configuration {}", questionDTO, id);
    return configService.removeQuestionFromConfiguration(id, questionId);
  }

  @PutMapping("/{id}/questions/{questionId}")
  public QuestionDTO updateQuestionFromConfiguration(
    @PathVariable final UUID id,
    @PathVariable final UUID questionId,
    @RequestBody final QuestionDTO questionDTO
  ) {
    log.debug("update question {} with {} for configuration {}", questionId, questionDTO, id);
    return configService.updateQuestionFromConfiguration(id, questionId, questionDTO);
  }
}
