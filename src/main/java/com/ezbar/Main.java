package com.ezbar;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@ComponentScan(basePackages = { "com.ezbar", "Controladores" })
@EntityScan(basePackages = "ClasesBD")
@EnableJpaRepositories(basePackages = "Repositorios")
public class Main {

    public static void main(String[] args) {
        org.springframework.context.ApplicationContext ctx = SpringApplication.run(Main.class, args);
        String port = ctx.getEnvironment().getProperty("server.port", "8080");
        String protocol = "true".equals(ctx.getEnvironment().getProperty("server.ssl.enabled")) ? "https" : "http";
        System.out.println("✓ API REST disponible en " + protocol + "://localhost:" + port);
    }
}
