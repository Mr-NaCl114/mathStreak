package com.lods.app;

import com.lods.domain.status.service.IStatusService;
import jakarta.annotation.Resource;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages = "com.lods")
@MapperScan("com.lods.infrastructure.dao")
@Configurable
@EnableScheduling
public class Application implements CommandLineRunner {

    @Resource
    private IStatusService IStatusService;

    static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(String... args) {
        IStatusService.initCurrentAnswer();
    }
}
