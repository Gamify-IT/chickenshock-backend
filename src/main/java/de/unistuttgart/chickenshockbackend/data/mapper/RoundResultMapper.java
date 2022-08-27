package de.unistuttgart.chickenshockbackend.data.mapper;

import de.unistuttgart.chickenshockbackend.data.Question;
import de.unistuttgart.chickenshockbackend.data.RoundResult;
import de.unistuttgart.chickenshockbackend.data.RoundResultDTO;
import de.unistuttgart.chickenshockbackend.repositories.QuestionRepository;
import java.util.ArrayList;
import java.util.List;
import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@Mapper(componentModel = "spring")
public abstract class RoundResultMapper {

  @Autowired
  QuestionRepository questionRepository;

  public RoundResult roundResultDTOToRoundResult(final RoundResultDTO roundResultDTO) {
    Question question = questionRepository
      .findById(roundResultDTO.getQuestionUUId())
      .orElseThrow(() ->
        new ResponseStatusException(
          HttpStatus.NOT_FOUND,
          String.format("There is no question with uuid %s.", roundResultDTO.getQuestionUUId())
        )
      );
    return new RoundResult(question, roundResultDTO.getAnswer());
  }

  public List<RoundResult> roundResultDTOsToRoundResults(final List<RoundResultDTO> roundResultDTOS) {
    List<RoundResult> roundResults = new ArrayList<>();
    roundResultDTOS.forEach(roundResultDTO -> {
      roundResults.add(roundResultDTOToRoundResult(roundResultDTO));
    });
    return roundResults;
  }
}
