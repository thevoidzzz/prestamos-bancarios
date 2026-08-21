package com.makers.prestamos.infrastructure.persistence;

import com.makers.prestamos.domain.model.Prestamo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PrestamoJpaRepository extends JpaRepository<Prestamo, Long> {

    List<Prestamo> findBySolicitanteIdOrderByFechaSolicitudDesc(Long solicitanteId);

    @Query("select p from Prestamo p join fetch p.solicitante order by p.fechaSolicitud desc")
    List<Prestamo> findAllConSolicitante();

    @Query("select p from Prestamo p join fetch p.solicitante left join fetch p.decididoPor where p.id = :id")
    Optional<Prestamo> findDetalle(Long id);
}
