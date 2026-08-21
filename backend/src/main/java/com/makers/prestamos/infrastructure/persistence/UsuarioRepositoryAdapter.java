package com.makers.prestamos.infrastructure.persistence;

import com.makers.prestamos.domain.model.Usuario;
import com.makers.prestamos.domain.port.UsuarioRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class UsuarioRepositoryAdapter implements UsuarioRepositoryPort {

    private final UsuarioJpaRepository jpa;

    public UsuarioRepositoryAdapter(UsuarioJpaRepository jpa) {
        this.jpa = jpa;
    }

    @Override
    public Usuario guardar(Usuario usuario) {
        return jpa.save(usuario);
    }

    @Override
    public Optional<Usuario> buscarPorUsername(String username) {
        return jpa.findByUsername(username);
    }

    @Override
    public Optional<Usuario> buscarPorId(Long id) {
        return jpa.findById(id);
    }

    @Override
    public List<Usuario> listar() {
        return jpa.findAll();
    }

    @Override
    public boolean existeUsername(String username) {
        return jpa.existsByUsername(username);
    }
}
