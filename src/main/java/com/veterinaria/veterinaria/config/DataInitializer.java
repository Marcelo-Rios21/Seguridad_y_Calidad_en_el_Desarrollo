package com.veterinaria.veterinaria.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.veterinaria.veterinaria.model.Rol;
import com.veterinaria.veterinaria.model.Usuario;
import com.veterinaria.veterinaria.repository.UsuarioRepository;

@Configuration
public class DataInitializer {
    @Bean
    public CommandLineRunner initUsuarios(UsuarioRepository usuarioRepository,
                                          PasswordEncoder passwordEncoder) {
        return args -> {
            if (usuarioRepository.count() == 0) {
                usuarioRepository.save(new Usuario(
                        null,
                        "admin",
                        passwordEncoder.encode("Adminvet1234#"),
                        Rol.ADMIN
                ));

                usuarioRepository.save(new Usuario(
                        null,
                        "vet",
                        passwordEncoder.encode("VetMed1234#"),
                        Rol.VETERINARIO
                ));

                usuarioRepository.save(new Usuario(
                        null,
                        "recepcion",
                        passwordEncoder.encode("RecepVet1234#"),
                        Rol.RECEPCION
                ));
            }
        };
    }
}
