package com.ezbar.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import com.ezbar.security.CustomUserDetailsService;

import static org.springframework.security.config.Customizer.withDefaults;

/**
 * Configuración de seguridad.
 * Configura HTTPS, CORS y autenticación.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Value("${ezbar.security.https-required:true}")
    private boolean httpsRequired;

    private final CustomUserDetailsService userDetailsService;

    public SecurityConfig(CustomUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
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
                        .requestMatchers("/setup/**").hasRole("ADMIN")
                        .requestMatchers("/login/**").permitAll()
                        .anyRequest().authenticated())
                .cors(cors -> cors.disable())
                .csrf(csrf -> csrf.disable())
                .userDetailsService(userDetailsService)
                .httpBasic(withDefaults());

        return http.build();
    }
}
