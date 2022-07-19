package de.unistuttgart.moorhuhnbackend.repositories;

import de.unistuttgart.moorhuhnbackend.data.Question;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionRepository extends JpaRepository<Question, UUID> {}
