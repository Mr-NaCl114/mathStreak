package com.lods.app.config;

import com.lods.trigger.Intercptor.HostValidationInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${app.ai-answer.allowed-hosts:localhost,127.0.0.1}")
    private List<String> allowedHosts;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new HostValidationInterceptor(allowedHosts))
                .addPathPatterns("/api/game/ai_answer/new_generate");
    }
}
