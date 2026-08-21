package com.makers.prestamos.application.dto;

import jakarta.validation.constraints.NotNull;

public class DecisionPrestamoRequest {

    @NotNull
    private Boolean aprobar;

    private String comentario;

    public Boolean getAprobar() {
        return aprobar;
    }

    public void setAprobar(Boolean aprobar) {
        this.aprobar = aprobar;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }
}
