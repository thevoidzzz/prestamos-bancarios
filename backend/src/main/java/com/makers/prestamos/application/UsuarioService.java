package com.makers.prestamos.application;

import com.makers.prestamos.application.dto.CrearUsuarioRequest;
import com.makers.prestamos.application.dto.UsuarioResponse;
import com.makers.prestamos.domain.exception.NegocioException;
import com.makers.prestamos.domain.model.Usuario;
import com.makers.prestamos.domain.port.UsuarioRepositoryPort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UsuarioService {

    private final UsuarioRepositoryPort usuarios;
    private final PasswordEncoder encoder;

    public UsuarioService(UsuarioRepositoryPort usuarios, PasswordEncoder encoder) {
        this.usuarios = usuarios;
        this.encoder = encoder;
    }

    @Transactional
    public UsuarioResponse crear(CrearUsuarioRequest request) {
        if (usuarios.existeUsername(request.getUsername())) {
            throw new NegocioException("Ese usuario ya existe");
        }
        Usuario nuevo = new Usuario(
                request.getUsername(),
                encoder.encode(request.getPassword()),
                request.getNombreCompleto(),
                request.getRol()
        );
        Usuario guardado = usuarios.guardar(nuevo);
        return toResponse(guardado);
    }

    @Transactional(readOnly = true)
    public List<UsuarioResponse> listar() {
        return usuarios.listar().stream().map(this::toResponse).toList();
    }

    public Usuario buscarPorUsername(String username) {
        return usuarios.buscarPorUsername(username)
                .orElseThrow(() -> new NegocioException("Usuario no encontrado", 404));
    }

    private UsuarioResponse toResponse(Usuario usuario) {
        return new UsuarioResponse(usuario.getId(), usuario.getUsername(), usuario.getNombreCompleto(), usuario.getRol());
    }
}
