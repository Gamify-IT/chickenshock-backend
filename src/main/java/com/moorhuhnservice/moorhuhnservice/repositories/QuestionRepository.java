package com.moorhuhnservice.moorhuhnservice.repositories;

import com.moorhuhnservice.moorhuhnservice.data.Configuration;
import com.moorhuhnservice.moorhuhnservice.data.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {}
