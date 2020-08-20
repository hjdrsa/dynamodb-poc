package com.hjdrsa.dynamodb.poc;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(
        title = "DynamoDB poc",
        description = "A rest api that uses dynamodb for storage",
        version = "0.0.1",
        contact = @Contact(
                name = "Henry-John Davis",
                email = "hjdrsa@gmail.com"
        )
))
public class Application {

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }
}
