package de.unistuttgart.chickenshockbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(scanBasePackages = { "de.unistuttgart" })
@EnableFeignClients
public class ChickenshockServiceApplication {

    public static void main(final String[] args) {
        SpringApplication.run(ChickenshockServiceApplication.class, args);
    }
}
