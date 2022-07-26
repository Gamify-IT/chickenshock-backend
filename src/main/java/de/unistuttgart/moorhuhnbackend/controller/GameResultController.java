package de.unistuttgart.moorhuhnbackend.controller;

import de.unistuttgart.moorhuhnbackend.data.GameResultDTO;
import de.unistuttgart.moorhuhnbackend.data.QuestionDTO;
import de.unistuttgart.moorhuhnbackend.repositories.GameResultRepository;
import de.unistuttgart.moorhuhnbackend.service.ConfigService;
import de.unistuttgart.moorhuhnbackend.service.GameResultService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("api/v1/minigames/moorhuhn/results")
@Slf4j
public class GameResultController {

    @Autowired
    GameResultService gameResultService;

    @PostMapping("/")
    @ResponseStatus(HttpStatus.CREATED)
    public GameResultDTO saveGameResult(
            @RequestBody final GameResultDTO gameResultDTO
    ) {
        log.debug("save game result");
        gameResultService.saveGameResult(gameResultDTO);
        return gameResultDTO;
    }
}
