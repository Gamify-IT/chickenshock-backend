package com.moorhuhnservice.moorhuhnservice.repositories;

import com.moorhuhnservice.moorhuhnservice.data.Configuration;
import com.moorhuhnservice.moorhuhnservice.data.Question;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConfigurationRepository extends JpaRepository<Configuration, Long> {
  Configuration findByName(String name);
  boolean existsByName(String name);
}
