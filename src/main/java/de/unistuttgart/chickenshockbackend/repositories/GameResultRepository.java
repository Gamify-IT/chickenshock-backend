package de.unistuttgart.chickenshockbackend.repositories;

import de.unistuttgart.chickenshockbackend.data.GameResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameResultRepository extends JpaRepository<GameResult, Long> {
}
