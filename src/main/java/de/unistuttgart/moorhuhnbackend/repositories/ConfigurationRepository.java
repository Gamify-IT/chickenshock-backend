package de.unistuttgart.moorhuhnbackend.repositories;

import de.unistuttgart.moorhuhnbackend.data.Configuration;
import java.util.UUID;

import de.unistuttgart.moorhuhnbackend.data.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConfigurationRepository extends JpaRepository<Configuration, UUID> {
    Question findByText(String questionText);
}
