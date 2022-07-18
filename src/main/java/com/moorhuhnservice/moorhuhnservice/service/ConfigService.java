package com.moorhuhnservice.moorhuhnservice.service;

import com.moorhuhnservice.moorhuhnservice.data.*;
import com.moorhuhnservice.moorhuhnservice.data.mapper.ConfigurationMapper;
import com.moorhuhnservice.moorhuhnservice.data.mapper.QuestionMapper;
import com.moorhuhnservice.moorhuhnservice.repositories.ConfigurationRepository;
import com.moorhuhnservice.moorhuhnservice.repositories.QuestionRepository;
import java.util.Optional;
import java.util.Set;
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
   * Search a configuration by given name
   *
   * @throws ResponseStatusException when configuration by configurationName could not be found
   * @param configurationName the name of the configuration searching for
   * @return the found configuration
   */
  public Configuration getConfiguration(final String configurationName) {
    final Configuration configuration = configurationRepository.findByName(configurationName);
    if (configuration == null) {
      throw new ResponseStatusException(
        HttpStatus.NOT_FOUND,
        "There is no configuration with configurationName " + configurationName
      );
    }
    return configuration;
  }

  /**
   * Save a configuration
   *
   * @throws ResponseStatusException when configuration with the name already exists
   * @param configurationDTO configuration that should be saved
   * @return the saved configuration
   */
  public Configuration saveConfiguration(final ConfigurationDTO configurationDTO) {
    if (configurationRepository.existsByName(configurationDTO.getName())) {
      throw new ResponseStatusException(
        HttpStatus.BAD_REQUEST,
        "Configuration with name " + configurationDTO.getName() + " already exists."
      );
    }
    final Configuration configuration = configurationMapper.configurationDTOToConfiguration(configurationDTO);
    final Configuration savedConfiguration = configurationRepository.save(configuration);
    return savedConfiguration;
  }

  /**
   * Update a configuration
   *
   * @throws ResponseStatusException when configuration with the name does not exist
   * @param configurationName the name of the configuration that should be updated
   * @param configurationDTO configuration that should be updated
   * @return the updated configuration
   */
  public Configuration updateConfiguration(final String configurationName, final ConfigurationDTO configurationDTO) {
    configurationDTO.setName(configurationName);
    final Configuration configuration = getConfiguration(configurationName);
    final Set<Question> questions = questionMapper.questionDTOsToQuestions(configurationDTO.getQuestions());
    configuration.setQuestions(questions);
    final Configuration updatedConfiguration = configurationRepository.save(configuration);
    return updatedConfiguration;
  }

  /**
   * Delete a configuration
   *
   * @throws ResponseStatusException when configuration with the name does not exist
   * @param configurationName the name of the configuration that should be updated
   * @return the updated configuration
   */
  public ConfigurationDTO deleteConfiguration(final String configurationName) {
    final Configuration configuration = getConfiguration(configurationName);
    final ConfigurationDTO deletedConfigurationDTO = configurationMapper.configurationToConfigurationDTO(configuration);
    configurationRepository.delete(configuration);
    return deletedConfigurationDTO;
  }

  /**
   * Add a question to specific configuration
   *
   * @throws ResponseStatusException when configurationName does not exist
   * @param configurationName the name of the configuration where a question should be added
   * @param questionDTO the question that should be added
   * @return the added question
   */
  public Question addQuestionToConfiguration(final String configurationName, final QuestionDTO questionDTO) {
    final Configuration configuration = getConfiguration(configurationName);
    final Question question = questionMapper.questionDTOToQuestion(questionDTO);
    final Question savedQuestion = questionRepository.save(question);
    configuration.addQuestion(savedQuestion);
    configurationRepository.save(configuration);
    return savedQuestion;
  }

  /**
   * Delete a question from a specific configuration
   *
   * @throws ResponseStatusException when configuration with the name or question with id does not exist
   * @param configurationName the name of the configuration where a question should be removed
   * @param questionId the id of the question that should be deleted
   * @return the deleted question as DTO
   */
  public QuestionDTO removeQuestionFromConfiguration(final String configurationName, final long questionId) {
    final Configuration configuration = getConfiguration(configurationName);
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
   * @throws ResponseStatusException when configuration with the name or question with id does not exist
   * @param configurationName the name of the configuration where a question should be updated
   * @param questionId the id of the question that should be updated
   * @param questionDTO the content of the question that should be updated
   * @return the updated question
   */
  public Question updateQuestionFromConfiguration(
    final String configurationName,
    final long questionId,
    final QuestionDTO questionDTO
  ) {
    final Configuration configuration = getConfiguration(configurationName);
    final Optional<Question> optionalQuestion = getQuestionInConfiguration(questionId, configuration);
    if (optionalQuestion.isEmpty()) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Question with " + questionId + " does not exist.");
    }
    final Question question = questionMapper.questionDTOToQuestion(questionDTO);
    question.setId(questionId);
    final Question savedQuestion = questionRepository.save(question);
    return savedQuestion;
  }

  /**
   *
   * @throws ResponseStatusException when question with the id in the given configuration does not exist
   * @param questionId id of searched question
   * @param configuration in which configuration the question is part of
   * @return an optional of the question
   */
  private Optional<Question> getQuestionInConfiguration(long questionId, Configuration configuration) {
    return configuration
      .getQuestions()
      .stream()
      .filter(filteredQuestion -> filteredQuestion.getId() == questionId)
      .findFirst();
  }
}
