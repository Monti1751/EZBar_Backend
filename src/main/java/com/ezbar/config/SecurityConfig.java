package com.ezbar.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Configuración de seguridad.
 * Configura HTTPS, CORS y autenticación.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Value("${ezbar.security.https-required:false}")
    private boolean httpsRequired;

    /**
     * Codificador de contraseñas usando BCrypt.
     * 
     * @return PasswordEncoder
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    /**
     * Configurar la cadena de filtros de seguridad.
     * 
     * @param http HttpSecurity
     * @return SecurityFilterChain
     * @throws Exception Si hay error en la configuración
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http)
            throws Exception {

        if (httpsRequired) {
            http.requiresChannel()
                    .anyRequest()
                    .requiresSecure();
        }

        http
                .authorizeHttpRequests((authz) -> authz
                        .requestMatchers("/actuator/health").permitAll()
                        .requestMatchers("/api/setup/**", "/setup/**").permitAll()
                        .requestMatchers("/api/login/**", "/login/**").permitAll()
                        .requestMatchers("/api/pagos/**", "/pagos/**").permitAll()
                        .requestMatchers("/api/pedidos/**", "/pedidos/**").permitAll()
                        .anyRequest().authenticated())
                .cors()
                .and()
                .csrf().disable()
                .httpBasic();

        return http.build();
    }
}
