package com.moorhuhnservice.moorhuhnservice.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.moorhuhnservice.moorhuhnservice.data.*;
import com.moorhuhnservice.moorhuhnservice.data.mapper.ConfigurationMapper;
import com.moorhuhnservice.moorhuhnservice.data.mapper.QuestionMapper;
import com.moorhuhnservice.moorhuhnservice.repositories.ConfigurationRepository;
import java.util.List;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@Slf4j
public class MoorhuhnService {

  @Autowired
  QuestionMapper questionMapper;

  @Autowired
  ConfigurationMapper configurationMapper;

  @Autowired
  ConfigurationRepository configurationRepository;

  public boolean isAuthorized(String tokenCookie) {
    boolean authorized = false;
    try {
      Algorithm algorithm = Algorithm.HMAC256("test"); //use more secure key
      JWTVerifier verifier = JWT.require(algorithm).build(); //Reusable verifier instance
      DecodedJWT jwt = verifier.verify(tokenCookie);
      authorized = true;
      log.debug("verification successfully! id was: {}", jwt.getClaim("id"));
    } catch (JWTVerificationException exception) {
      log.debug("verification not successfully.", exception);
    }
    return authorized;
  }

  /**
   * Search a configuration by given name
   *
   * @throws ResponseStatusException when configuration by configurationName could not be found
   * @param configurationName the name of the configuration searching for
   * @return the found configuration
   */
  public Configuration getConfiguration(String configurationName) {
    Configuration configuration = configurationRepository.findByName(configurationName);
    if (configuration == null) {
      throw new ResponseStatusException(
        HttpStatus.NOT_FOUND,
        "There is no configuration with configurationName " + configurationName
      );
    }
    return configuration;
  }

  /**
   * Save a configuration by a given ConfigurationDTO
   *
   * @throws ResponseStatusException when configuration with the name already exists
   * @param configurationDTO configuration that should be saved
   * @return the saved configuration
   */
  public Configuration saveConfiguration(ConfigurationDTO configurationDTO) {
    if (configurationRepository.existsByName(configurationDTO.getName())) {
      throw new ResponseStatusException(
        HttpStatus.BAD_REQUEST,
        "Configuration with name " + configurationDTO.getName() + " already exists."
      );
    }
    log.info("ConfigurationDTO object: {}", configurationDTO);
    Configuration configuration = configurationMapper.configurationDTOToConfiguration(configurationDTO);
    log.info("Configuration object: {}", configuration);
    Configuration savedConfiguration = configurationRepository.save(configuration);
    return savedConfiguration;
  }

  /**
   * Update a configuration by a given ConfigurationDTO
   *
   * @throws ResponseStatusException when configuration with the name does not exist
   * @param configurationName the name of the configuration that should be updated
   * @param configurationDTO configuration that should be updated
   * @return the updated configuration
   */
  public Configuration updateConfiguration(String configurationName, ConfigurationDTO configurationDTO) {
    configurationDTO.setName(configurationName);
    if (!configurationRepository.existsByName(configurationDTO.getName())) {
      throw new ResponseStatusException(
        HttpStatus.NOT_FOUND,
        "Configuration with name " + configurationDTO.getName() + " does not exist."
      );
    }
    Configuration configuration = configurationRepository.findByName(configurationDTO.getName());
    Set<Question> questions = questionMapper.questionDTOsToQuestions(configurationDTO.getQuestions());
    configuration.setQuestions(questions);
    Configuration updatedConfiguration = configurationRepository.save(configuration);
    return updatedConfiguration;
  }
}
