package com.cris.bank_app_backend.controllers;

import com.cris.bank_app_backend.services.SolicitudService;
import org.springframework.http.ResponseEntity;

import com.cris.bank_app_backend.entities.PrestamoEntity;
import com.cris.bank_app_backend.services.PrestamoService;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@RestController
@RequestMapping("/prestamos")
public class PrestamoController {

    @Autowired
    private PrestamoService prestamoService;

    @Autowired
    private SolicitudService solicitudService;

    // Endpoint para guardar un nuevo préstamo
    @PostMapping("/guardar")
    public ResponseEntity<PrestamoEntity> guardarPrestamo(@RequestBody PrestamoEntity prestamo) {
        return ResponseEntity.ok(prestamoService.guardarPrestamo(prestamo));
    }

    // Endpoint para obtener todos los préstamos
    @GetMapping("/")
    public ResponseEntity<List<PrestamoEntity>> obtenerTodosLosPrestamos() {
        return ResponseEntity.ok(prestamoService.obtenerTodosLosPrestamos());
    }

    // Endpoint para obtener un préstamo por su id
    @GetMapping("/{id}")
    public ResponseEntity<PrestamoEntity> obtenerPrestamoPorId(@PathVariable Long id) {
        return ResponseEntity.ok(prestamoService.obtenerPrestamoPorId(id));
    }

    // Endpoint para eliminar un préstamo por su id
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarPrestamo(@PathVariable Long id) {
        prestamoService.eliminarPrestamo(id);
        return ResponseEntity.ok("Préstamo eliminado correctamente.");
    }

    // Endpoint para encontrar un préstamo por el id de la solicitud
    @GetMapping("/solicitud/{id}")
    public ResponseEntity<PrestamoEntity> obtenerPrestamoPorSolicitudId(@PathVariable Long id) {
        return ResponseEntity.ok(prestamoService.obtenerPrestamoPorSolicitudId(id));
    }

    @PostMapping("/{id}/respuesta")
    public ResponseEntity<String> responderPrestamo(@PathVariable Long id, @RequestParam boolean aceptar) {
        if (aceptar) {
            solicitudService.actualizarEstadoSolicitud(id, "Aprobada");
            return ResponseEntity.ok("Solicitud aprobada.");
        } else {
            solicitudService.actualizarEstadoSolicitud(id, "Cancelada por el cliente");
            return ResponseEntity.ok("Solicitud cancelada.");
        }
    }
}
