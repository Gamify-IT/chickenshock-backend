package com.moorhuhnservice.moorhuhnservice.repositories;

import com.moorhuhnservice.moorhuhnservice.data.Configuration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConfigurationRepository extends JpaRepository<Configuration, Long> {
  Configuration findByName(final String name);
  boolean existsByName(final String name);
}
