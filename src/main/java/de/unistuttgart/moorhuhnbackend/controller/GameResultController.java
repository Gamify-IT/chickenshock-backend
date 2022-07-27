package de.unistuttgart.moorhuhnbackend.controller;

import de.unistuttgart.moorhuhnbackend.data.GameResultDTO;
import de.unistuttgart.moorhuhnbackend.service.GameResultService;
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

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public GameResultDTO saveGameResult(
            @CookieValue("token") String tokenCookie,
            @RequestBody final GameResultDTO gameResultDTO
    ) {
        log.debug("save game result");
        gameResultService.saveGameResult(gameResultDTO, tokenCookie);
        return gameResultDTO;
    }
}
