package com.conexa.test.dev.backend;

import com.conexa.test.dev.backend.config.ConfigProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(ConfigProperties.class)
public class TesteDevBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(TesteDevBackendApplication.class, args);
    }

}
