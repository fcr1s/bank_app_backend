package com.cris.bank_app_backend.controllers;

import com.cris.bank_app_backend.entities.ClienteEntity;
import com.cris.bank_app_backend.entities.EjecutivoEntity;
import com.cris.bank_app_backend.entities.SolicitudEntity;
import com.cris.bank_app_backend.services.EjecutivoService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;


@RestController
@RequestMapping("/ejecutivos")
@CrossOrigin("*")
public class EjecutivoController {

    @Autowired
    private EjecutivoService ejecutivoService;

    // Endpoint para iniciar sesión del ejecutivo
    @PostMapping("/login")
    public ResponseEntity<EjecutivoEntity> login(@RequestParam String rut, @RequestParam String password) {
        EjecutivoEntity ejecutivo = ejecutivoService.login(rut, password);
        return ejecutivo != null ? ResponseEntity.ok(ejecutivo) : ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    // Endpoint para cerrar sesión del ejecutivo
    @PostMapping("/logout")
    public ResponseEntity<String> logout() {
        ejecutivoService.logout();
        return ResponseEntity.ok("Sesión cerrada");
    }

    // Endpoint para que el ejecutivo consulte todas las solicitudes o filtre por estado
    @GetMapping("/solicitudes")
    public ResponseEntity<List<SolicitudEntity>> obtenerSolicitudes(
            @RequestParam(required = false) String estado) {
        if (!ejecutivoService.estaLogueado()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        List<SolicitudEntity> solicitudes;
        if (estado != null && !estado.isEmpty()) {
            solicitudes = ejecutivoService.obtenerSolicitudesPorEstado(estado);
        } else {
            solicitudes = ejecutivoService.obtenerTodasLasSolicitudes();
        }
        return ResponseEntity.ok(solicitudes);
    }

    // Endpoint para actualizar el estado de una solicitud
    @PutMapping("/solicitudes/{id}/estado")
    public ResponseEntity<String> actualizarEstadoSolicitud(
            @PathVariable Long id, @RequestParam String nuevoEstado) {
        if (!ejecutivoService.estaLogueado()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        try {
            ejecutivoService.actualizarEstadoSolicitud(id, nuevoEstado);
            return ResponseEntity.ok("Estado actualizado exitosamente.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}


