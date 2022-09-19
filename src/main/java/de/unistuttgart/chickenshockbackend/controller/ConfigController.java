package de.unistuttgart.chickenshockbackend.controller;

import de.unistuttgart.chickenshockbackend.data.ConfigurationDTO;
import de.unistuttgart.chickenshockbackend.data.QuestionDTO;
import de.unistuttgart.chickenshockbackend.data.mapper.ConfigurationMapper;
import de.unistuttgart.chickenshockbackend.repositories.ConfigurationRepository;
import de.unistuttgart.chickenshockbackend.service.ConfigService;
import de.unistuttgart.gamifyit.authentificationvalidator.JWTValidatorService;
import java.util.List;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * This controller handles all game-configuration-related REST-APIs
 */
@RestController
@RequestMapping("/configurations")
@Import({ JWTValidatorService.class })
@Slf4j
public class ConfigController {

    @Autowired
    private ConfigurationRepository configurationRepository;

    @Autowired
    private ConfigService configService;

    @Autowired
    private JWTValidatorService jwtValidatorService;

    @Autowired
    private ConfigurationMapper configurationMapper;

    @GetMapping("")
    public List<ConfigurationDTO> getConfigurations(@CookieValue("access_token") final String accessToken) {
        jwtValidatorService.validateTokenOrThrow(accessToken);
        log.debug("get all configurations");
        return configurationMapper.configurationsToConfigurationDTOs(configurationRepository.findAll());
    }

    @GetMapping("/{id}")
    public ConfigurationDTO getConfiguration(
        @CookieValue("access_token") final String accessToken,
        @PathVariable final UUID id
    ) {
        jwtValidatorService.validateTokenOrThrow(accessToken);
        log.debug("get configuration {}", id);
        return configurationMapper.configurationToConfigurationDTO(configService.getConfiguration(id));
    }

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public ConfigurationDTO createConfiguration(
        @CookieValue("access_token") final String accessToken,
        @RequestBody final ConfigurationDTO configurationDTO
    ) {
        jwtValidatorService.validateTokenOrThrow(accessToken);
        jwtValidatorService.hasRolesOrThrow(accessToken, List.of("lecturer"));
        log.debug("create configuration {}", configurationDTO);
        return configService.saveConfiguration(configurationDTO);
    }

    @PutMapping("/{id}")
    public ConfigurationDTO updateConfiguration(
        @CookieValue("access_token") final String accessToken,
        @PathVariable final UUID id,
        @RequestBody final ConfigurationDTO configurationDTO
    ) {
        jwtValidatorService.validateTokenOrThrow(accessToken);
        jwtValidatorService.hasRolesOrThrow(accessToken, List.of("lecturer"));
        log.debug("update configuration {} with {}", id, configurationDTO);
        return configService.updateConfiguration(id, configurationDTO);
    }

    @DeleteMapping("/{id}")
    public ConfigurationDTO deleteConfiguration(
        @CookieValue("access_token") final String accessToken,
        @PathVariable final UUID id
    ) {
        jwtValidatorService.validateTokenOrThrow(accessToken);
        jwtValidatorService.hasRolesOrThrow(accessToken, List.of("lecturer"));
        log.debug("delete configuration {}", id);
        return configService.deleteConfiguration(id);
    }

    @PostMapping("/{id}/questions")
    @ResponseStatus(HttpStatus.CREATED)
    public QuestionDTO addQuestionToConfiguration(
        @CookieValue("access_token") final String accessToken,
        @PathVariable final UUID id,
        @RequestBody final QuestionDTO questionDTO
    ) {
        jwtValidatorService.validateTokenOrThrow(accessToken);
        jwtValidatorService.hasRolesOrThrow(accessToken, List.of("lecturer"));
        log.debug("add question {} to configuration {}", questionDTO, id);
        return configService.addQuestionToConfiguration(id, questionDTO);
    }

    @DeleteMapping("/{id}/questions/{questionId}")
    public QuestionDTO removeQuestionFromConfiguration(
        @CookieValue("access_token") final String accessToken,
        @PathVariable final UUID id,
        @PathVariable final UUID questionId
    ) {
        jwtValidatorService.validateTokenOrThrow(accessToken);
        jwtValidatorService.hasRolesOrThrow(accessToken, List.of("lecturer"));
        log.debug("remove question {} from configuration {}", questionId, id);
        return configService.removeQuestionFromConfiguration(id, questionId);
    }

    @PutMapping("/{id}/questions/{questionId}")
    public QuestionDTO updateQuestionFromConfiguration(
        @CookieValue("access_token") final String accessToken,
        @PathVariable final UUID id,
        @PathVariable final UUID questionId,
        @RequestBody final QuestionDTO questionDTO
    ) {
        jwtValidatorService.validateTokenOrThrow(accessToken);
        jwtValidatorService.hasRolesOrThrow(accessToken, List.of("lecturer"));
        log.debug("update question {} with {} for configuration {}", questionId, questionDTO, id);
        return configService.updateQuestionFromConfiguration(id, questionId, questionDTO);
    }
}
