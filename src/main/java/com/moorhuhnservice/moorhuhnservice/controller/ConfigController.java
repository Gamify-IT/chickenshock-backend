package com.moorhuhnservice.moorhuhnservice.controller;

import com.moorhuhnservice.moorhuhnservice.data.*;
import com.moorhuhnservice.moorhuhnservice.data.mapper.ConfigurationMapper;
import com.moorhuhnservice.moorhuhnservice.data.mapper.QuestionMapper;
import com.moorhuhnservice.moorhuhnservice.repositories.ConfigurationRepository;
import com.moorhuhnservice.moorhuhnservice.service.ConfigService;
import java.util.List;
import java.util.Set;
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
  public List<Configuration> getConfigurations() {
    log.debug("get all configurations");
    List<Configuration> configurations = configurationRepository.findAll();
    return configurations;
  }

  @GetMapping("/{configurationName}")
  public Configuration getConfiguration(@PathVariable String configurationName) {
    log.debug("get configuration {}", configurationName);
    return configService.getConfiguration(configurationName);
  }

  @GetMapping("/{configurationName}/questions")
  public Set<Question> getQuestionsFromConfiguration(@PathVariable String configurationName) {
    log.debug("get questions from configuration {}", configurationName);
    return configService.getConfiguration(configurationName).getQuestions();
  }

  @PostMapping("")
  @ResponseStatus(HttpStatus.CREATED)
  public Configuration createConfiguration(@RequestBody ConfigurationDTO configurationDTO) {
    log.debug("create configuration {}", configurationDTO);
    return configService.saveConfiguration(configurationDTO);
  }

  @PutMapping("/{configurationName}")
  public Configuration updateConfiguration(
    @PathVariable String configurationName,
    @RequestBody ConfigurationDTO configurationDTO
  ) {
    log.debug("update {} configuration {}", configurationName, configurationDTO);
    return configService.updateConfiguration(configurationName, configurationDTO);
  }
}