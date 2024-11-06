package com.swpgavf.back.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // Allow requests from frontend URL and configure methods
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:5173")  // Allow frontend (React) origin
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")  // Allow all headers
                .allowCredentials(false);  // No cookies/authorization headers needed
    }
}
