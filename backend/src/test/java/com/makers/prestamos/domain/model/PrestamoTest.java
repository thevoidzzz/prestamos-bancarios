package com.makers.prestamos.domain.model;

import com.makers.prestamos.domain.exception.NegocioException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PrestamoTest {

    @Test
    void apruebaUnaSolicitudPendiente() {
        Usuario admin = new Usuario("admin", "x", "Laura", Rol.ADMIN);
        Usuario maria = new Usuario("maria", "x", "Maria", Rol.CLIENTE);
        Prestamo prestamo = Prestamo.nuevaSolicitud(maria, new BigDecimal("2000000"), 12);

        prestamo.decidir(admin, true, "Score aceptable");

        assertEquals(EstadoPrestamo.APROBADO, prestamo.getEstado());
        assertEquals(admin, prestamo.getDecididoPor());
    }

    @Test
    void noDejaRechazarSinMotivo() {
        Usuario admin = new Usuario("admin", "x", "Laura", Rol.ADMIN);
        Usuario maria = new Usuario("maria", "x", "Maria", Rol.CLIENTE);
        Prestamo prestamo = Prestamo.nuevaSolicitud(maria, new BigDecimal("2000000"), 12);

        NegocioException ex = assertThrows(NegocioException.class,
                () -> prestamo.decidir(admin, false, "  "));
        assertEquals("Para rechazar hay que indicar un motivo", ex.getMessage());
        assertEquals(EstadoPrestamo.PENDIENTE, prestamo.getEstado());
    }

    @Test
    void noSePuedeDecidirDosVeces() {
        Usuario admin = new Usuario("admin", "x", "Laura", Rol.ADMIN);
        Usuario maria = new Usuario("maria", "x", "Maria", Rol.CLIENTE);
        Prestamo prestamo = Prestamo.nuevaSolicitud(maria, new BigDecimal("2000000"), 12);
        prestamo.decidir(admin, true, "ok");

        assertThrows(NegocioException.class, () -> prestamo.decidir(admin, false, "tarde"));
    }

    @Test
    void unClienteNoAprueba() {
        Usuario maria = new Usuario("maria", "x", "Maria", Rol.CLIENTE);
        Prestamo prestamo = Prestamo.nuevaSolicitud(maria, new BigDecimal("2000000"), 12);

        NegocioException ex = assertThrows(NegocioException.class,
                () -> prestamo.decidir(maria, true, "yo misma"));
        assertEquals(403, ex.getHttpStatus());
    }
}
