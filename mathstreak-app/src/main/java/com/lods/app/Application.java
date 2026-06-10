package com.lods.app;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages = "com.lods")
@MapperScan("com.lods.infrastructure.dao")
@Configurable
@EnableScheduling
public class Application {

    static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
