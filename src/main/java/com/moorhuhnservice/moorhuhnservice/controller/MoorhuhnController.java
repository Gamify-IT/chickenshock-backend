package com.moorhuhnservice.moorhuhnservice.controller;

import com.moorhuhnservice.moorhuhnservice.data.*;
import com.moorhuhnservice.moorhuhnservice.data.mapper.ConfigurationMapper;
import com.moorhuhnservice.moorhuhnservice.data.mapper.QuestionMapper;
import com.moorhuhnservice.moorhuhnservice.repositories.ConfigurationRepository;
import com.moorhuhnservice.moorhuhnservice.service.MoorhuhnService;
import java.util.List;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("api/v1/minigames/moorhuhn")
@Slf4j
public class MoorhuhnController {

  @Autowired
  ConfigurationRepository configurationRepository;

  @Autowired
  MoorhuhnService moorhuhnService;

  @Autowired
  QuestionMapper questionMapper;

  @Autowired
  ConfigurationMapper configurationMapper;

  @GetMapping("/configurations")
  public List<Configuration> getConfigurations() {
    log.debug("get all configurations");
    List<Configuration> configurations = configurationRepository.findAll();
    return configurations;
  }

  @GetMapping("/configurations/{configurationName}")
  public Configuration getConfiguration(@PathVariable String configurationName) {
    log.debug("get configuration {}", configurationName);
    return moorhuhnService.getConfiguration(configurationName);
  }

  @GetMapping("/configurations/{configurationName}/questions")
  public Set<Question> getQuestionsFromConfiguration(@PathVariable String configurationName) {
    log.debug("get questions from configuration {}", configurationName);
    return moorhuhnService.getConfiguration(configurationName).getQuestions();
  }

  @PostMapping("/configurations")
  @ResponseStatus(HttpStatus.CREATED)
  public Configuration createConfiguration(@RequestBody ConfigurationDTO configurationDTO) {
    log.debug("create configuration {}", configurationDTO);
    return moorhuhnService.saveConfiguration(configurationDTO);
  }

  @PutMapping("/configurations/{configurationName}")
  public Configuration updateConfiguration(
    @PathVariable String configurationName,
    @RequestBody ConfigurationDTO configurationDTO
  ) {
    log.debug("create configuration {}", configurationDTO);
    return moorhuhnService.updateConfiguration(configurationName, configurationDTO);
  }
}
