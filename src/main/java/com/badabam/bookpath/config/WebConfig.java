package com.badabam.bookpath.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // 경로 허용
        .allowedOrigins("http://localhost:3000").allowedMethods(
                HttpMethod.GET.name(),
                HttpMethod.POST.name()
        );
        //allowedMethod //enum타입으로
    }
}
