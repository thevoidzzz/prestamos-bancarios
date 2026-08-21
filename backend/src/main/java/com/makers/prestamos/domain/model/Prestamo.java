package com.makers.prestamos.domain.model;

import com.makers.prestamos.domain.exception.NegocioException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "prestamos")
public class Prestamo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "solicitante_id")
    private Usuario solicitante;

    @Column(nullable = false, precision = 14, scale = 2)
    private BigDecimal monto;

    @Column(nullable = false)
    private Integer plazoMeses;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EstadoPrestamo estado;

    @Column(nullable = false)
    private Instant fechaSolicitud;

    private Instant fechaDecision;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "decidido_por_id")
    private Usuario decididoPor;

    @Column(length = 255)
    private String comentarioDecision;

    public Prestamo() {
    }

    public static Prestamo nuevaSolicitud(Usuario solicitante, BigDecimal monto, Integer plazoMeses) {
        if (solicitante.esAdmin()) {
            throw new NegocioException("Un administrador no solicita prestamos desde esta cuenta");
        }
        Prestamo prestamo = new Prestamo();
        prestamo.solicitante = solicitante;
        prestamo.monto = monto;
        prestamo.plazoMeses = plazoMeses;
        prestamo.estado = EstadoPrestamo.PENDIENTE;
        return prestamo;
    }

    @PrePersist
    void alCrear() {
        if (fechaSolicitud == null) {
            fechaSolicitud = Instant.now();
        }
        if (estado == null) {
            estado = EstadoPrestamo.PENDIENTE;
        }
    }

    public void decidir(Usuario admin, boolean aprobar, String comentario) {
        admin.asegurarEsAdmin();
        if (estado != EstadoPrestamo.PENDIENTE) {
            throw new NegocioException("Esta solicitud ya fue atendida");
        }
        if (!aprobar && (comentario == null || comentario.isBlank())) {
            throw new NegocioException("Para rechazar hay que indicar un motivo");
        }
        this.estado = aprobar ? EstadoPrestamo.APROBADO : EstadoPrestamo.RECHAZADO;
        this.decididoPor = admin;
        this.fechaDecision = Instant.now();
        this.comentarioDecision = comentario;
    }

    public boolean perteneceA(Usuario usuario) {
        return solicitante.getId().equals(usuario.getId());
    }

    public Long getId() {
        return id;
    }

    public Usuario getSolicitante() {
        return solicitante;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public Integer getPlazoMeses() {
        return plazoMeses;
    }

    public EstadoPrestamo getEstado() {
        return estado;
    }

    public Instant getFechaSolicitud() {
        return fechaSolicitud;
    }

    public Instant getFechaDecision() {
        return fechaDecision;
    }

    public Usuario getDecididoPor() {
        return decididoPor;
    }

    public String getComentarioDecision() {
        return comentarioDecision;
    }
}
