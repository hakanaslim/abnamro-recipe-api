package com.abnamro.recipe;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.abnamro")
@OpenAPIDefinition(info = @Info(title = "${spring.application.name}", description = "Recipe Information", version = "1.0"))
public class AbnAmroApplication {

    public static void main(String[] args) {
        SpringApplication.run(AbnAmroApplication.class, args);
    }

}
