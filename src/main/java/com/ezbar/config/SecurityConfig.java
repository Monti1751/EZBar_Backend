package com.ezbar.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
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

    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder encoder) {
        UserDetails admin = User.withUsername("admin")
                .password(encoder.encode("admin123"))
                .roles("ADMIN")
                .build();
        return new InMemoryUserDetailsManager(admin);
    }

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
                        .requestMatchers("/setup/**").permitAll()
                        .requestMatchers("/login/**").permitAll()
                        .anyRequest().authenticated())
                .cors(cors -> cors.disable())
                .csrf(csrf -> csrf.disable())
                .httpBasic();

        return http.build();
    }
}
