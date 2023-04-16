package de.unistuttgart.chickenshockbackend.controller;

import de.unistuttgart.chickenshockbackend.data.ConfigurationDTO;
import de.unistuttgart.chickenshockbackend.data.QuestionDTO;
import de.unistuttgart.chickenshockbackend.data.mapper.ConfigurationMapper;
import de.unistuttgart.chickenshockbackend.repositories.ConfigurationRepository;
import de.unistuttgart.chickenshockbackend.service.ConfigService;
import de.unistuttgart.gamifyit.authentificationvalidator.JWTValidatorService;
import io.swagger.v3.oas.annotations.Operation;
import java.util.List;
import java.util.UUID;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * This controller handles all game-configuration-related REST-APIs
 */
@RestController
@RequestMapping("/configurations")
@Import({ JWTValidatorService.class })
@Slf4j
@Validated
public class ConfigController {

    public static final List<String> LECTURER = List.of("lecturer");
    @Autowired
    private ConfigurationRepository configurationRepository;

    @Autowired
    private ConfigService configService;

    @Autowired
    private JWTValidatorService jwtValidatorService;

    @Autowired
    private ConfigurationMapper configurationMapper;

    @Operation(summary = "Get all configurations")
    @GetMapping("")
    public List<ConfigurationDTO> getConfigurations(@CookieValue("access_token") final String accessToken) {
        jwtValidatorService.validateTokenOrThrow(accessToken);
        log.debug("get all configurations");
        return configurationMapper.configurationsToConfigurationDTOs(configurationRepository.findAll());
    }

    @Operation(summary = "Get a specific configuration by its id")
    @GetMapping("/{id}")
    public ConfigurationDTO getConfiguration(
        @CookieValue("access_token") final String accessToken,
        @PathVariable final UUID id
    ) {
        jwtValidatorService.validateTokenOrThrow(accessToken);
        log.debug("get configuration {}", id);
        return configurationMapper.configurationToConfigurationDTO(configService.getConfiguration(id));
    }

    @Operation(summary = "Create a new configuration")
    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public ConfigurationDTO createConfiguration(
        @CookieValue("access_token") final String accessToken,
        @Valid @RequestBody final ConfigurationDTO configurationDTO
    ) {
        jwtValidatorService.validateTokenOrThrow(accessToken);
        jwtValidatorService.hasRolesOrThrow(accessToken, LECTURER);
        log.debug("create configuration {}", configurationDTO);
        return configService.saveConfiguration(configurationDTO);
    }

    @Operation(summary = "Update a configuration")
    @PutMapping("/{id}")
    public ConfigurationDTO updateConfiguration(
        @CookieValue("access_token") final String accessToken,
        @PathVariable final UUID id,
        @Valid @RequestBody final ConfigurationDTO configurationDTO
    ) {
        jwtValidatorService.validateTokenOrThrow(accessToken);
        jwtValidatorService.hasRolesOrThrow(accessToken, LECTURER);
        log.debug("update configuration {} with {}", id, configurationDTO);
        return configService.updateConfiguration(id, configurationDTO);
    }

    @Operation(summary = "Delete a configuration")
    @DeleteMapping("/{id}")
    public ConfigurationDTO deleteConfiguration(
        @CookieValue("access_token") final String accessToken,
        @PathVariable final UUID id
    ) {
        jwtValidatorService.validateTokenOrThrow(accessToken);
        jwtValidatorService.hasRolesOrThrow(accessToken, LECTURER);
        log.debug("delete configuration {}", id);
        return configService.deleteConfiguration(id);
    }

    @Operation(summary = "Add multiple questions to a configuration")
    @PostMapping("/{id}/questions")
    @ResponseStatus(HttpStatus.CREATED)
    public QuestionDTO addQuestionToConfiguration(
        @CookieValue("access_token") final String accessToken,
        @PathVariable final UUID id,
        @Valid @RequestBody final QuestionDTO questionDTO
    ) {
        jwtValidatorService.validateTokenOrThrow(accessToken);
        jwtValidatorService.hasRolesOrThrow(accessToken, LECTURER);
        log.debug("add question {} to configuration {}", questionDTO, id);
        return configService.addQuestionToConfiguration(id, questionDTO);
    }

    @Operation(summary = "Delete a question from a configuration")
    @DeleteMapping("/{id}/questions/{questionId}")
    public QuestionDTO removeQuestionFromConfiguration(
        @CookieValue("access_token") final String accessToken,
        @PathVariable final UUID id,
        @PathVariable final UUID questionId
    ) {
        jwtValidatorService.validateTokenOrThrow(accessToken);
        jwtValidatorService.hasRolesOrThrow(accessToken, LECTURER);
        log.debug("remove question {} from configuration {}", questionId, id);
        return configService.removeQuestionFromConfiguration(id, questionId);
    }

    @Operation(summary = "Update a question in a configuration")
    @PutMapping("/{id}/questions/{questionId}")
    public QuestionDTO updateQuestionFromConfiguration(
        @CookieValue("access_token") final String accessToken,
        @PathVariable final UUID id,
        @PathVariable final UUID questionId,
        @Valid @RequestBody final QuestionDTO questionDTO
    ) {
        jwtValidatorService.validateTokenOrThrow(accessToken);
        jwtValidatorService.hasRolesOrThrow(accessToken, LECTURER);
        log.debug("update question {} with {} for configuration {}", questionId, questionDTO, id);
        return configService.updateQuestionFromConfiguration(id, questionId, questionDTO);
    }

    @PostMapping("/{id}/clone")
    @ResponseStatus(HttpStatus.CREATED)
    public UUID cloneConfiguration(@CookieValue("access_token") final String accessToken, @PathVariable final UUID id) {
        jwtValidatorService.validateTokenOrThrow(accessToken);
        jwtValidatorService.hasRolesOrThrow(accessToken, LECTURER);
        return configService.cloneConfiguration(id);
    }
}
