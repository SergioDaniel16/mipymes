package edu.gt.umg.mipymes.contable.mipymes_contable_api.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuración web para CORS y otras configuraciones HTTP
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     * Configuración CORS para permitir requests desde Angular
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins("http://localhost:4200", "http://localhost:3000") // Angular y otros frontends
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}