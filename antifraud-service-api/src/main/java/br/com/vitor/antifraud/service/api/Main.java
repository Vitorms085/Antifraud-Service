package br.com.vitor.antifraud.service.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = {
    "br.com.vitor.antifraud.service.api",
    "br.com.vitor.antifraud.service.core"
})
@EnableJpaRepositories(basePackages = "br.com.vitor.antifraud.service.core")
@EntityScan("br.com.vitor.antifraud.service.model")
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}