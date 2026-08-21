package com.makers.prestamos.application.dto;

import com.makers.prestamos.domain.model.EstadoPrestamo;

public class EstadoPrestamoResponse {

    private Long id;
    private EstadoPrestamo estado;
    private String comentarioDecision;

    public EstadoPrestamoResponse() {
    }

    public EstadoPrestamoResponse(Long id, EstadoPrestamo estado, String comentarioDecision) {
        this.id = id;
        this.estado = estado;
        this.comentarioDecision = comentarioDecision;
    }

    public Long getId() {
        return id;
    }

    public EstadoPrestamo getEstado() {
        return estado;
    }

    public String getComentarioDecision() {
        return comentarioDecision;
    }
}
