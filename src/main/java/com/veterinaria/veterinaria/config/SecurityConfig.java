package com.veterinaria.veterinaria.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.veterinaria.veterinaria.security.CustomUserDetailsService;

@Configuration
public class SecurityConfig {
private final CustomUserDetailsService customUserDetailsService;

    public SecurityConfig(CustomUserDetailsService customUserDetailsService) {
        this.customUserDetailsService = customUserDetailsService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) {
        try {
            http
                .csrf(csrf -> csrf.ignoringRequestMatchers("/api/**"))
                .authorizeHttpRequests(auth -> auth
                    .requestMatchers("/", "/login", "/css/**").permitAll()
                    .requestMatchers("/api/auth/login").permitAll()
                    .requestMatchers("/dashboard").authenticated()
                    .requestMatchers("/pacientes").hasAnyRole("ADMIN", "VETERINARIO", "RECEPCION")
                    .requestMatchers("/pacientes/nuevo", "/pacientes/guardar").hasAnyRole("ADMIN", "RECEPCION")
                    .requestMatchers("/citas").hasAnyRole("ADMIN", "VETERINARIO", "RECEPCION")
                    .requestMatchers("/citas/nueva", "/citas/guardar").hasAnyRole("ADMIN", "RECEPCION")
                    .anyRequest().authenticated()
                )
                .formLogin(form -> form
                    .loginPage("/login")
                    .defaultSuccessUrl("/dashboard", true)
                    .failureUrl("/login?error")
                    .permitAll()
                )
                .logout(logout -> logout
                    .logoutSuccessUrl("/login?logout")
                    .permitAll()
                )
                .authenticationProvider(authenticationProvider());

            return http.build();
        } catch (Exception e) {
            throw new IllegalStateException("Error al configurar Spring Security.", e);
        }
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(customUserDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) {
        try {
            return configuration.getAuthenticationManager();
        } catch (Exception e) {
            throw new IllegalStateException("No se pudo crear AuthenticationManager.", e);
        }
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
