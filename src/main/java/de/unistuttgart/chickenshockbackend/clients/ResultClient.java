package de.unistuttgart.chickenshockbackend.clients;

import de.unistuttgart.chickenshockbackend.data.OverworldResultDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(value = "resultClient", url = "${overworld.url}/internal")
public interface ResultClient {
    @PostMapping("/submit-game-pass")
    void submit(OverworldResultDTO resultDTO);
}
