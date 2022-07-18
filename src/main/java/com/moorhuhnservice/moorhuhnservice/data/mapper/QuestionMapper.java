package com.moorhuhnservice.moorhuhnservice.data.mapper;

import com.moorhuhnservice.moorhuhnservice.data.Question;
import com.moorhuhnservice.moorhuhnservice.data.QuestionDTO;
import java.util.Set;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface QuestionMapper {
  QuestionDTO questionToQuestionDTO(final Question Question);

  Question questionDTOToQuestion(final QuestionDTO questionDTO);

  Set<Question> questionDTOsToQuestions(final Set<QuestionDTO> questionDTOs);

  Set<QuestionDTO> questionsToQuestionDTOs(final Set<Question> questions);
}
