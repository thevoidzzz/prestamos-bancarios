package com.makers.prestamos.infrastructure.config;

import com.makers.prestamos.domain.model.Rol;
import com.makers.prestamos.domain.model.Usuario;
import com.makers.prestamos.domain.port.UsuarioRepositoryPort;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DatosIniciales implements CommandLineRunner {

    private final UsuarioRepositoryPort usuarios;
    private final PasswordEncoder encoder;

    public DatosIniciales(UsuarioRepositoryPort usuarios, PasswordEncoder encoder) {
        this.usuarios = usuarios;
        this.encoder = encoder;
    }

    @Override
    public void run(String... args) {
        if (usuarios.existeUsername("admin@test.com")) {
            return;
        }
        usuarios.guardar(new Usuario("admin@test.com", encoder.encode("123"), "Admin Admin", Rol.ADMIN));
        usuarios.guardar(new Usuario("usuario@test.com", encoder.encode("123"), "Usuario", Rol.CLIENTE));
    }
}
