package com.makers.prestamos.application.dto;

import com.makers.prestamos.domain.model.EstadoPrestamo;

import java.math.BigDecimal;
import java.time.Instant;

public class PrestamoResponse {

    private Long id;
    private String solicitante;
    private BigDecimal monto;
    private Integer plazoMeses;
    private EstadoPrestamo estado;
    private Instant fechaSolicitud;
    private Instant fechaDecision;
    private String comentarioDecision;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSolicitante() {
        return solicitante;
    }

    public void setSolicitante(String solicitante) {
        this.solicitante = solicitante;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }

    public Integer getPlazoMeses() {
        return plazoMeses;
    }

    public void setPlazoMeses(Integer plazoMeses) {
        this.plazoMeses = plazoMeses;
    }

    public EstadoPrestamo getEstado() {
        return estado;
    }

    public void setEstado(EstadoPrestamo estado) {
        this.estado = estado;
    }

    public Instant getFechaSolicitud() {
        return fechaSolicitud;
    }

    public void setFechaSolicitud(Instant fechaSolicitud) {
        this.fechaSolicitud = fechaSolicitud;
    }

    public Instant getFechaDecision() {
        return fechaDecision;
    }

    public void setFechaDecision(Instant fechaDecision) {
        this.fechaDecision = fechaDecision;
    }

    public String getComentarioDecision() {
        return comentarioDecision;
    }

    public void setComentarioDecision(String comentarioDecision) {
        this.comentarioDecision = comentarioDecision;
    }
}
