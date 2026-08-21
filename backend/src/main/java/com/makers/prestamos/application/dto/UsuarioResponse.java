package com.makers.prestamos.application.dto;

import com.makers.prestamos.domain.model.Rol;

public class UsuarioResponse {

    private Long id;
    private String username;
    private String nombreCompleto;
    private Rol rol;

    public UsuarioResponse(Long id, String username, String nombreCompleto, Rol rol) {
        this.id = id;
        this.username = username;
        this.nombreCompleto = nombreCompleto;
        this.rol = rol;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public Rol getRol() {
        return rol;
    }
}
