package com.moorhuhnservice.moorhuhnservice.data.mapper;

import com.moorhuhnservice.moorhuhnservice.data.Question;
import com.moorhuhnservice.moorhuhnservice.data.QuestionDTO;
import java.util.List;
import java.util.Set;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface QuestionMapper {
  QuestionDTO questionToQuestionDTO(Question Question);

  Question questionDTOToQuestion(QuestionDTO questionDTO);

  Set<Question> questionDTOsToQuestions(Set<QuestionDTO> questionDTOs);

  Set<QuestionDTO> questionsToQuestionDTOs(Set<Question> questions);
}
