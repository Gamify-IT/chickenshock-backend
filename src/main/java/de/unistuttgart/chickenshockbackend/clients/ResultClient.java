package de.unistuttgart.chickenshockbackend.clients;

import de.unistuttgart.chickenshockbackend.data.OverworldResultDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * This client's purpose is to send an OverworldResultDTO to the Overworld-Backend when a Player finished a Game.
 */
@FeignClient(value = "resultClient", url = "${overworld.url}/internal")
public interface ResultClient {
    /**
     * Submits the resultDTO to the Overworld-Backend
     *
     * @param resultDTO the player submitted result, trimmed down to the data needed for the overworld
     */
    @PostMapping("/submit-game-pass")
    void submit(OverworldResultDTO resultDTO);
}
