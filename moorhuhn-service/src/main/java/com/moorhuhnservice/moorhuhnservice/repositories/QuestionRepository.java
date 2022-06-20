package com.moorhuhnservice.moorhuhnservice.repositories;

import com.moorhuhnservice.moorhuhnservice.BaseClasses.Question;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionRepository extends CrudRepository<Question, Long>{
}
