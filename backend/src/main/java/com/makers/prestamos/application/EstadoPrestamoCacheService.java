package com.makers.prestamos.application;

import com.makers.prestamos.application.dto.EstadoPrestamoResponse;
import com.makers.prestamos.domain.exception.NegocioException;
import com.makers.prestamos.domain.model.Prestamo;
import com.makers.prestamos.domain.port.PrestamoRepositoryPort;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class EstadoPrestamoCacheService {

    private final PrestamoRepositoryPort prestamos;

    public EstadoPrestamoCacheService(PrestamoRepositoryPort prestamos) {
        this.prestamos = prestamos;
    }

    @Cacheable(cacheNames = "estadoPrestamos", key = "#id")
    public EstadoPrestamoResponse leer(Long id) {
        Prestamo prestamo = prestamos.buscarPorId(id)
                .orElseThrow(() -> new NegocioException("Prestamo no encontrado", 404));
        return new EstadoPrestamoResponse(prestamo.getId(), prestamo.getEstado(), prestamo.getComentarioDecision());
    }

    @CacheEvict(cacheNames = "estadoPrestamos", key = "#id")
    public void invalidar(Long id) {
        // Spring se encarga de sacar la entrada. Lo dejo como metodo para llamarlo despues de decidir.
    }
}
