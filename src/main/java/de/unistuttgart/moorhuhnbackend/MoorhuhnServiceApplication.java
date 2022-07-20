package de.unistuttgart.moorhuhnbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class MoorhuhnServiceApplication {

  public static void main(String[] args) {
    SpringApplication.run(MoorhuhnServiceApplication.class, args);
  }
}
