package com.makers.prestamos.infrastructure.security;

import com.makers.prestamos.application.dto.LoginRequest;
import com.makers.prestamos.application.dto.TokenResponse;
import com.makers.prestamos.domain.exception.NegocioException;
import com.makers.prestamos.domain.model.Usuario;
import com.makers.prestamos.domain.port.UsuarioRepositoryPort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UsuarioRepositoryPort usuarios;
    private final PasswordEncoder encoder;
    private final JwtService jwtService;

    public AuthService(UsuarioRepositoryPort usuarios, PasswordEncoder encoder, JwtService jwtService) {
        this.usuarios = usuarios;
        this.encoder = encoder;
        this.jwtService = jwtService;
    }

    public TokenResponse login(LoginRequest request) {
        Usuario usuario = usuarios.buscarPorUsername(request.getUsername())
                .orElseThrow(() -> new NegocioException("Usuario o clave incorrectos", 401));
        if (!encoder.matches(request.getPassword(), usuario.getPassword())) {
            throw new NegocioException("Usuario o clave incorrectos", 401);
        }
        String token = jwtService.generar(usuario);
        return new TokenResponse(token, usuario.getUsername(), usuario.getNombreCompleto(), usuario.getRol());
    }
}
