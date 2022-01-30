package org.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@EnableJpaRepositories
@SpringBootApplication(scanBasePackages = {"org.example"})
public class App {
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}
