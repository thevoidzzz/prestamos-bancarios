package com.makers.prestamos.infrastructure.web;

import com.makers.prestamos.application.PrestamoService;
import com.makers.prestamos.application.UsuarioService;
import com.makers.prestamos.application.dto.DecisionPrestamoRequest;
import com.makers.prestamos.application.dto.EstadoPrestamoResponse;
import com.makers.prestamos.application.dto.PrestamoResponse;
import com.makers.prestamos.application.dto.SolicitudPrestamoRequest;
import com.makers.prestamos.domain.model.Usuario;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/prestamos")
public class PrestamoController {

    private final PrestamoService prestamoService;
    private final UsuarioService usuarioService;

    public PrestamoController(PrestamoService prestamoService, UsuarioService usuarioService) {
        this.prestamoService = prestamoService;
        this.usuarioService = usuarioService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PrestamoResponse solicitar(@Valid @RequestBody SolicitudPrestamoRequest request, Principal principal) {
        Usuario usuario = usuarioService.buscarPorUsername(principal.getName());
        return prestamoService.solicitar(usuario, request);
    }

    @GetMapping
    public List<PrestamoResponse> listar(Principal principal) {
        Usuario usuario = usuarioService.buscarPorUsername(principal.getName());
        return prestamoService.listar(usuario);
    }

    @GetMapping("/{id}")
    public PrestamoResponse detalle(@PathVariable Long id, Principal principal) {
        Usuario usuario = usuarioService.buscarPorUsername(principal.getName());
        return prestamoService.detalle(id, usuario);
    }

    @GetMapping("/{id}/estado")
    public EstadoPrestamoResponse estado(@PathVariable Long id, Principal principal) {
        Usuario usuario = usuarioService.buscarPorUsername(principal.getName());
        return prestamoService.consultarEstado(id, usuario);
    }

    @PutMapping("/{id}/decision")
    @PreAuthorize("hasRole('ADMIN')")
    public PrestamoResponse decidir(
            @PathVariable Long id,
            @Valid @RequestBody DecisionPrestamoRequest request,
            Principal principal
    ) {
        Usuario admin = usuarioService.buscarPorUsername(principal.getName());
        return prestamoService.decidir(id, admin, request);
    }
}
