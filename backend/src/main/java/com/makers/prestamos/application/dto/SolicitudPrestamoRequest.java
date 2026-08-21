package com.makers.prestamos.application.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class SolicitudPrestamoRequest {

    @NotNull
    @DecimalMin(value = "1000", message = "El monto minimo es 1.000")
    @DecimalMax(value = "50000000", message = "El monto maximo es 50.000.000")
    private BigDecimal monto;

    @NotNull
    @Min(value = 6, message = "El plazo minimo es 6 meses")
    @Max(value = 60, message = "El plazo maximo es 60 meses")
    private Integer plazoMeses;

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
}
