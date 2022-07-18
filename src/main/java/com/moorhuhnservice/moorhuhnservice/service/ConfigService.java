package com.moorhuhnservice.moorhuhnservice.service;

import com.moorhuhnservice.moorhuhnservice.data.*;
import com.moorhuhnservice.moorhuhnservice.data.mapper.ConfigurationMapper;
import com.moorhuhnservice.moorhuhnservice.data.mapper.QuestionMapper;
import com.moorhuhnservice.moorhuhnservice.repositories.ConfigurationRepository;
import com.moorhuhnservice.moorhuhnservice.repositories.QuestionRepository;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@Slf4j
@Transactional
public class ConfigService {

  @Autowired
  QuestionMapper questionMapper;

  @Autowired
  ConfigurationMapper configurationMapper;

  @Autowired
  ConfigurationRepository configurationRepository;

  @Autowired
  QuestionRepository questionRepository;

  /**
   * Search a configuration by given id
   *
   * @throws ResponseStatusException when configuration by configurationName could not be found
   * @param id the id of the configuration searching for
   * @return the found configuration
   */
  public Configuration getConfiguration(final UUID id) {
    final Optional<Configuration> configuration = configurationRepository.findById(id);
    if (configuration.isEmpty()) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "There is no configuration with id " + id);
    }
    return configuration.get();
  }

  /**
   * Save a configuration
   *
   * @param configurationDTO configuration that should be saved
   * @return the saved configuration as DTO
   */
  public ConfigurationDTO saveConfiguration(final ConfigurationDTO configurationDTO) {
    final Configuration configuration = configurationMapper.configurationDTOToConfiguration(configurationDTO);
    final Configuration savedConfiguration = configurationRepository.save(configuration);
    final ConfigurationDTO savedConfigurationDTO = configurationMapper.configurationToConfigurationDTO(
      savedConfiguration
    );
    return savedConfigurationDTO;
  }

  /**
   * Update a configuration
   *
   * @throws ResponseStatusException when configuration with the id does not exist
   * @param id the id of the configuration that should be updated
   * @param configurationDTO configuration that should be updated
   * @return the updated configuration as DTO
   */
  public ConfigurationDTO updateConfiguration(final UUID id, final ConfigurationDTO configurationDTO) {
    final Configuration configuration = getConfiguration(id);
    final Set<Question> questions = questionMapper.questionDTOsToQuestions(configurationDTO.getQuestions());
    configuration.setQuestions(questions);
    final Configuration updatedConfiguration = configurationRepository.save(configuration);
    final ConfigurationDTO updatedConfigurationDTO = configurationMapper.configurationToConfigurationDTO(
      updatedConfiguration
    );
    return updatedConfigurationDTO;
  }

  /**
   * Delete a configuration
   *
   * @throws ResponseStatusException when configuration with the id does not exist
   * @param id the id of the configuration that should be updated
   * @return the deleted configuration as DTO
   */
  public ConfigurationDTO deleteConfiguration(final UUID id) {
    final Configuration configuration = getConfiguration(id);
    final ConfigurationDTO deletedConfigurationDTO = configurationMapper.configurationToConfigurationDTO(configuration);
    configurationRepository.delete(configuration);
    return deletedConfigurationDTO;
  }

  /**
   * Add a question to specific configuration
   *
   * @throws ResponseStatusException when configuration with the id does not exist
   * @param id the id of the configuration where a question should be added
   * @param questionDTO the question that should be added
   * @return the added question as DTO
   */
  public QuestionDTO addQuestionToConfiguration(final UUID id, final QuestionDTO questionDTO) {
    final Configuration configuration = getConfiguration(id);
    final Question question = questionMapper.questionDTOToQuestion(questionDTO);
    final Question savedQuestion = questionRepository.save(question);
    final QuestionDTO savedQuestionDTO = questionMapper.questionToQuestionDTO(savedQuestion);
    configuration.addQuestion(savedQuestion);
    configurationRepository.save(configuration);
    return savedQuestionDTO;
  }

  /**
   * Delete a question from a specific configuration
   *
   * @throws ResponseStatusException when configuration with the id or question with id does not exist
   * @param id the id of the configuration where a question should be removed
   * @param questionId the id of the question that should be deleted
   * @return the deleted question as DTO
   */
  public QuestionDTO removeQuestionFromConfiguration(final UUID id, final UUID questionId) {
    final Configuration configuration = getConfiguration(id);
    final Optional<Question> optionalQuestion = getQuestionInConfiguration(questionId, configuration);
    if (optionalQuestion.isEmpty()) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Question with " + questionId + " does not exist.");
    }
    final Question question = optionalQuestion.get();
    final QuestionDTO deletedQuestionDTO = questionMapper.questionToQuestionDTO(question);
    configuration.removeQuestion(question);
    configurationRepository.save(configuration);
    questionRepository.delete(question);
    return deletedQuestionDTO;
  }

  /**
   * Update a question from a specific configuration
   *
   * @throws ResponseStatusException when configuration with the id or question with id does not exist
   * @param id the id of the configuration where a question should be updated
   * @param questionId the id of the question that should be updated
   * @param questionDTO the content of the question that should be updated
   * @return the updated question as DTO
   */
  public QuestionDTO updateQuestionFromConfiguration(
    final UUID id,
    final UUID questionId,
    final QuestionDTO questionDTO
  ) {
    final Configuration configuration = getConfiguration(id);
    final Optional<Question> optionalQuestion = getQuestionInConfiguration(questionId, configuration);
    if (optionalQuestion.isEmpty()) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Question with " + questionId + " does not exist.");
    }
    final Question question = questionMapper.questionDTOToQuestion(questionDTO);
    question.setId(questionId);
    final Question savedQuestion = questionRepository.save(question);
    final QuestionDTO savedQuestionDTO = questionMapper.questionToQuestionDTO(savedQuestion);
    return savedQuestionDTO;
  }

  /**
   *
   * @throws ResponseStatusException when question with the id in the given configuration does not exist
   * @param questionId id of searched question
   * @param configuration configuration in which the question is part of
   * @return an optional of the question
   */
  private Optional<Question> getQuestionInConfiguration(UUID questionId, Configuration configuration) {
    return configuration
      .getQuestions()
      .stream()
      .filter(filteredQuestion -> filteredQuestion.getId().equals(questionId))
      .findFirst();
  }
}
