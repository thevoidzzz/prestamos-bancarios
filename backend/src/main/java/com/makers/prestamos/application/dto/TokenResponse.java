package com.makers.prestamos.application.dto;

import com.makers.prestamos.domain.model.Rol;

public class TokenResponse {

    private String token;
    private String username;
    private String nombreCompleto;
    private Rol rol;

    public TokenResponse(String token, String username, String nombreCompleto, Rol rol) {
        this.token = token;
        this.username = username;
        this.nombreCompleto = nombreCompleto;
        this.rol = rol;
    }

    public String getToken() {
        return token;
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
