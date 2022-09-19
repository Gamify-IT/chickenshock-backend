package de.unistuttgart.chickenshockbackend.controller;

import de.unistuttgart.chickenshockbackend.data.GameResultDTO;
import de.unistuttgart.chickenshockbackend.service.GameResultService;
import de.unistuttgart.gamifyit.authentificationvalidator.JWTValidatorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/results")
@Import({JWTValidatorService.class})
@Slf4j
public class GameResultController {

  @Autowired
  private GameResultService gameResultService;

  @Autowired
  private JWTValidatorService jwtValidatorService;

  @PostMapping("")
  @ResponseStatus(HttpStatus.CREATED)
  public GameResultDTO saveGameResult(
    @CookieValue("access_token") final String accessToken,
    @RequestBody final GameResultDTO gameResultDTO
  ) {
    jwtValidatorService.validateTokenOrThrow(accessToken);
    final String userId = jwtValidatorService.extractUserId(accessToken);
    log.info("save game result for userId {}: {}", userId, gameResultDTO);
    gameResultService.saveGameResult(gameResultDTO, userId);
    return gameResultDTO;
  }
}
