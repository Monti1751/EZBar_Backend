package com.ezbar;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@ComponentScan(basePackages = { "com.ezbar", "Controladores", "Repositorios" })
@EntityScan(basePackages = "ClasesBD")
@EnableJpaRepositories(basePackages = "Repositorios")
public class Main {

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
        System.out.println("✓ API REST disponible en http://localhost:8080");
    }
}
