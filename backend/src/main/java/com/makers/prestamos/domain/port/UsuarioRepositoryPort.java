package com.makers.prestamos.domain.port;

import com.makers.prestamos.domain.model.Usuario;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepositoryPort {

    Usuario guardar(Usuario usuario);

    Optional<Usuario> buscarPorUsername(String username);

    Optional<Usuario> buscarPorId(Long id);

    List<Usuario> listar();

    boolean existeUsername(String username);
}
