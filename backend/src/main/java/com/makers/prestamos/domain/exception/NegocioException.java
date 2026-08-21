package com.makers.prestamos.domain.exception;

public class NegocioException extends RuntimeException {

    private final int httpStatus;

    public NegocioException(String mensaje) {
        this(mensaje, 422);
    }

    public NegocioException(String mensaje, int httpStatus) {
        super(mensaje);
        this.httpStatus = httpStatus;
    }

    public int getHttpStatus() {
        return httpStatus;
    }
}
