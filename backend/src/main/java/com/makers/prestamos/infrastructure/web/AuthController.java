package com.makers.prestamos.infrastructure.web;

import com.makers.prestamos.application.UsuarioService;
import com.makers.prestamos.application.dto.LoginRequest;
import com.makers.prestamos.application.dto.TokenResponse;
import com.makers.prestamos.application.dto.UsuarioResponse;
import com.makers.prestamos.domain.model.Usuario;
import com.makers.prestamos.infrastructure.security.AuthService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final UsuarioService usuarioService;

    public AuthController(AuthService authService, UsuarioService usuarioService) {
        this.authService = authService;
        this.usuarioService = usuarioService;
    }

    @PostMapping("/login")
    public TokenResponse login(@Valid @RequestBody LoginRequest request) {
        return authService.login(request);
    }

    @GetMapping("/me")
    public UsuarioResponse me(Principal principal) {
        Usuario usuario = usuarioService.buscarPorUsername(principal.getName());
        return new UsuarioResponse(usuario.getId(), usuario.getUsername(), usuario.getNombreCompleto(), usuario.getRol());
    }
}
