package com.nexacrm.api.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // Bütün API-lar üçün
                .allowedOrigins("http://localhost:5173") // React-ın işləyəcəyi port
                .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH") // İcazə verilən metodlar
                .allowedHeaders("*");
    }
}