package de.unistuttgart.chickenshockbackend.controller;

import de.unistuttgart.chickenshockbackend.data.GameResultDTO;
import de.unistuttgart.chickenshockbackend.service.GameResultService;
import de.unistuttgart.gamifyit.authentificationvalidator.JWTValidatorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.net.MalformedURLException;

@RestController
@RequestMapping("/results")
@Slf4j
public class GameResultController {

  @Autowired
  private GameResultService gameResultService;

  @Value("${keycloak.issuer}")
  private String keycloakIssuer;

  private JWTValidatorService jwtValidatorService;

  @Autowired
  private void setJWTValidatorService() throws MalformedURLException {
    jwtValidatorService = new JWTValidatorService(keycloakIssuer);
  }

  @PostMapping("")
  @ResponseStatus(HttpStatus.CREATED)
  public GameResultDTO saveGameResult(
    @CookieValue("access_token") final String accessToken,
    @RequestBody final GameResultDTO gameResultDTO
  ) {
    final String userId = jwtValidatorService.validate(accessToken).getSubject();
    log.info("save game result for userId {}: {}", userId, gameResultDTO);
    gameResultService.saveGameResult(gameResultDTO, userId);
    return gameResultDTO;
  }
}
