package com.makers.prestamos.application;

import com.makers.prestamos.application.dto.DecisionPrestamoRequest;
import com.makers.prestamos.application.dto.EstadoPrestamoResponse;
import com.makers.prestamos.application.dto.PrestamoResponse;
import com.makers.prestamos.application.dto.SolicitudPrestamoRequest;
import com.makers.prestamos.domain.exception.NegocioException;
import com.makers.prestamos.domain.model.Prestamo;
import com.makers.prestamos.domain.model.Usuario;
import com.makers.prestamos.domain.port.PrestamoRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PrestamoService {

    private final PrestamoRepositoryPort prestamos;
    private final EstadoPrestamoCacheService cacheEstados;

    public PrestamoService(PrestamoRepositoryPort prestamos, EstadoPrestamoCacheService cacheEstados) {
        this.prestamos = prestamos;
        this.cacheEstados = cacheEstados;
    }

    @Transactional
    public PrestamoResponse solicitar(Usuario solicitante, SolicitudPrestamoRequest request) {
        Prestamo prestamo = Prestamo.nuevaSolicitud(solicitante, request.getMonto(), request.getPlazoMeses());
        return toResponse(prestamos.guardar(prestamo));
    }

    @Transactional
    public PrestamoResponse decidir(Long id, Usuario admin, DecisionPrestamoRequest request) {
        Prestamo prestamo = prestamos.buscarPorId(id)
                .orElseThrow(() -> new NegocioException("Prestamo no encontrado", 404));
        prestamo.decidir(admin, request.getAprobar(), request.getComentario());
        Prestamo guardado = prestamos.guardar(prestamo);
        cacheEstados.invalidar(id);
        return toResponse(guardado);
    }

    @Transactional(readOnly = true)
    public List<PrestamoResponse> listar(Usuario actual) {
        List<Prestamo> lista = actual.esAdmin()
                ? prestamos.listarTodos()
                : prestamos.listarDe(actual);
        return lista.stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public PrestamoResponse detalle(Long id, Usuario actual) {
        Prestamo prestamo = obtenerVisible(id, actual);
        return toResponse(prestamo);
    }

    @Transactional(readOnly = true)
    public EstadoPrestamoResponse consultarEstado(Long id, Usuario actual) {
        obtenerVisible(id, actual);
        return cacheEstados.leer(id);
    }

    private Prestamo obtenerVisible(Long id, Usuario actual) {
        Prestamo prestamo = prestamos.buscarPorId(id)
                .orElseThrow(() -> new NegocioException("Prestamo no encontrado", 404));
        if (!actual.esAdmin() && !prestamo.perteneceA(actual)) {
            throw new NegocioException("No puedes ver un prestamo de otra persona", 403);
        }
        return prestamo;
    }

    private PrestamoResponse toResponse(Prestamo prestamo) {
        PrestamoResponse dto = new PrestamoResponse();
        dto.setId(prestamo.getId());
        dto.setSolicitante(prestamo.getSolicitante().getUsername());
        dto.setMonto(prestamo.getMonto());
        dto.setPlazoMeses(prestamo.getPlazoMeses());
        dto.setEstado(prestamo.getEstado());
        dto.setFechaSolicitud(prestamo.getFechaSolicitud());
        dto.setFechaDecision(prestamo.getFechaDecision());
        dto.setComentarioDecision(prestamo.getComentarioDecision());
        return dto;
    }
}
