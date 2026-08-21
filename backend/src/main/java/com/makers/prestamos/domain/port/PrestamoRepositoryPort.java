package com.makers.prestamos.domain.port;

import com.makers.prestamos.domain.model.Prestamo;
import com.makers.prestamos.domain.model.Usuario;

import java.util.List;
import java.util.Optional;

public interface PrestamoRepositoryPort {

    Prestamo guardar(Prestamo prestamo);

    Optional<Prestamo> buscarPorId(Long id);

    List<Prestamo> listarDe(Usuario solicitante);

    List<Prestamo> listarTodos();
}
