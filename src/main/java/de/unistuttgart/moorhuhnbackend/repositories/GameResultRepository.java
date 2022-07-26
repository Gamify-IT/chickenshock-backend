package de.unistuttgart.moorhuhnbackend.repositories;

import de.unistuttgart.moorhuhnbackend.data.GameResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameResultRepository extends JpaRepository<GameResult, Long> {
}
