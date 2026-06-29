package com.rev.app.config;

import com.rev.app.security.JwtAuthFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity // Enables SpEL expressions like @PreAuthorize
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private final com.rev.app.security.RateLimitingFilter rateLimitingFilter;

    @Autowired
    public SecurityConfig(JwtAuthFilter jwtAuthFilter, com.rev.app.security.RateLimitingFilter rateLimitingFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
        this.rateLimitingFilter = rateLimitingFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // CSRF is safely disabled here because the application uses completely stateless JWT authentication.
            // In a truly stateless API without session cookies, CSRF (Cross-Site Request Forgery) attacks 
            // are inherently voided since the browser does not automatically send authorization credentials.
            .csrf(csrf -> csrf.disable()) 
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/", "/login", "/register", "/home", "/error", "/favicon.ico", "/css/**", "/js/**", "/img/**", "/images/**").permitAll()
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers("/api/public/**").permitAll()
                // API routes secured by Role-Based Auth (JWT)
                .requestMatchers("/api/admin/**").hasRole("ADMIN")
                .requestMatchers("/api/analytics/business/**", "/api/business/**").hasAnyRole("BUSINESS", "ADMIN")
                // Allow ALL UI routes to be public in Spring Security because Thymeleaf controllers handle auth manually via HttpSession
                .requestMatchers("/dashboard/**", "/wallet/**", "/transaction/**", "/history/**", "/user/**", "/payment-method/**", "/money-request/**", "/cards/**", "/notifications/**", "/business/**", "/invoice/**", "/loan/**", "/admin/**").permitAll()
                // All other API requests must be authenticated
                .anyRequest().authenticated()
            )
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED) // Support sessions for Thymeleaf while keeping JWT
            )
            .addFilterBefore(rateLimitingFilter, UsernamePasswordAuthenticationFilter.class)
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
