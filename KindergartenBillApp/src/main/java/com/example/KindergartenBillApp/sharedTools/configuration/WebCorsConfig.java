package com.example.KindergartenBillApp.sharedTools.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebCorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // Vazi za sve REST endpoint-e... videcemo da li ce se menjati
                .allowedOrigins("http://localhost:3000") // lokalni frontend... ovde cu dodavati po potrebi
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") //ne stavaljm patch jer se retko koristi, ali ako treba dodacu. Nemam pojma sta je "OPTIONS", ali neka ga
                .allowedHeaders("*") //dozvoljava sve headere (Authorization, Content-Type, itd.)
                .allowCredentials(true); // ovo je za cookies, odnosno u mom slucaju za JWT verovatno...
    }
}
