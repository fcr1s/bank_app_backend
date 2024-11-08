package com.cris.bank_app_backend.controllers;


import com.cris.bank_app_backend.entities.SolicitudEntity;
import com.cris.bank_app_backend.services.SolicitudService;
import org.springframework.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/solicitudes")
@CrossOrigin(origins = "http://4.228.227.122:8080")
public class SolicitudController {

    @Autowired
    private SolicitudService solicitudService;

    // Endpoint para obtener todas las solicitudes
    @GetMapping("/")
    public ResponseEntity<List<SolicitudEntity>> obtenerSolicitudes() {
        return ResponseEntity.ok(solicitudService.obtenerSolicitudes());
    }

    // Endpoint para obtener una solicitud por ID
    @GetMapping("/{id}")
    public ResponseEntity<SolicitudEntity> obtenerSolicitud(@PathVariable Long id) {
        SolicitudEntity solicitud = solicitudService.obtenerSolicitudPorId(id);
        return solicitud != null ? ResponseEntity.ok(solicitud) : ResponseEntity.notFound().build();
    }

    // Endpoint para eliminar una solicitud por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarSolicitud(@PathVariable Long id) {
        try {
            solicitudService.eliminarSolicitud(id);
            return ResponseEntity.ok("Solicitud eliminada correctamente.");
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/crear")
    public ResponseEntity<String> crearSolicitud(
            @RequestParam String tipoPrestamo,
            @RequestParam double valorPropiedad,
            @RequestParam double montoPrestamo,
            @RequestParam double tasaInteresAnual,
            @RequestParam int plazo,
            @RequestParam List<MultipartFile> documentos) { // Cambiado a List<MultipartFile>

        try {
            // Llama al servicio para crear la solicitud, pasando los documentos como archivos
            solicitudService.crearSolicitud(tipoPrestamo, valorPropiedad, montoPrestamo, tasaInteresAnual, plazo, documentos);
            return ResponseEntity.ok("Solicitud de préstamo creada en revisión inicial.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // Endpoint para consultar todas las solicitudes de un cliente
    @GetMapping("/mis-solicitudes")
    public ResponseEntity<List<SolicitudEntity>> obtenerSolicitudesDelCliente() {
        try {
            List<SolicitudEntity> solicitudes = solicitudService.obtenerSolicitudesDelCliente();
            return ResponseEntity.ok(solicitudes);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }

    // Endpoint para cancelar una solicitud de préstamo
    @PutMapping("/cancelar")
    public ResponseEntity<String> cancelarSolicitud(
            @RequestParam Long solicitudId) {
        try {
            solicitudService.cancelarSolicitud(solicitudId);
            return ResponseEntity.ok("Solicitud cancelada exitosamente.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
