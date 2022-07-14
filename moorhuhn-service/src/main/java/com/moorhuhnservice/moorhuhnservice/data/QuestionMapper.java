package com.moorhuhnservice.moorhuhnservice.data;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
@Component
public interface QuestionMapper {

    QuestionDTO questionToQuestionDTO(Question Question);

    Question questionDTOToQuestion(QuestionDTO questionDTO);
}
