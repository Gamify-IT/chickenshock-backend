package de.unistuttgart.chickenshockbackend.controller;

import de.unistuttgart.chickenshockbackend.data.GameResultDTO;
import de.unistuttgart.chickenshockbackend.service.GameResultService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/results")
@Slf4j
public class GameResultController {

  @Autowired
  GameResultService gameResultService;

  @Autowired
  JWTValidatorService jwtValidatorService;

  @PostMapping("")
  @ResponseStatus(HttpStatus.CREATED)
  public GameResultDTO saveGameResult(@CookieValue("access_token") final String accessToken, @RequestBody final GameResultDTO gameResultDTO) {
    final String userId = jwtValidatorService.validate(accessToken).getSubject();
    log.debug("save game result");
    gameResultService.saveGameResult(gameResultDTO, userId);
    return gameResultDTO;
  }
}
