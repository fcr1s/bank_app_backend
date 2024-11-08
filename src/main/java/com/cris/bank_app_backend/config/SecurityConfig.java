package com.cris.bank_app_backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // Configuración global de CORS
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                // Permite solicitudes de tu frontend, ajusta la URL
                registry.addMapping("/**")
                        .allowedOrigins("http://4.228.227.122:8080") // Cambia esta URL si es necesario
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders("*")
                        .allowCredentials(true); // Permite enviar cookies si es necesario
            }
        };
    }

    // Configuración de seguridad para Spring Security 6.1+
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((authz) -> authz
                        .anyRequest().permitAll() // Permitir todas las solicitudes
                )
                .csrf(csrf -> csrf.disable()) // Deshabilitar CSRF si no lo estás usando
                //habilitar cors
                .cors();


        return http.build();
    }
}
