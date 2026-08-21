package com.makers.prestamos.infrastructure.persistence;

import com.makers.prestamos.domain.model.Prestamo;
import com.makers.prestamos.domain.model.Usuario;
import com.makers.prestamos.domain.port.PrestamoRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class PrestamoRepositoryAdapter implements PrestamoRepositoryPort {

    private final PrestamoJpaRepository jpa;

    public PrestamoRepositoryAdapter(PrestamoJpaRepository jpa) {
        this.jpa = jpa;
    }

    @Override
    public Prestamo guardar(Prestamo prestamo) {
        return jpa.save(prestamo);
    }

    @Override
    public Optional<Prestamo> buscarPorId(Long id) {
        return jpa.findDetalle(id);
    }

    @Override
    public List<Prestamo> listarDe(Usuario solicitante) {
        return jpa.findBySolicitanteIdOrderByFechaSolicitudDesc(solicitante.getId());
    }

    @Override
    public List<Prestamo> listarTodos() {
        return jpa.findAllConSolicitante();
    }
}
