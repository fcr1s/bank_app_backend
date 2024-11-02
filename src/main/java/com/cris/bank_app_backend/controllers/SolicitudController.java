package com.cris.bank_app_backend.controllers;


import com.cris.bank_app_backend.entities.SolicitudEntity;
import com.cris.bank_app_backend.services.SolicitudService;
import com.cris.bank_app_backend.entities.DocumentoEntity;
import org.springframework.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/solicitudes")
@CrossOrigin("*")
public class SolicitudController {

    @Autowired
    private SolicitudService solicitudService;

    // Endpoint para obtener todas las solicitudes
    @GetMapping
    public ResponseEntity<List<SolicitudEntity>> obtenerSolicitudes() {
        return ResponseEntity.ok(solicitudService.obtenerSolicitudes());
    }

    // Endpoint para obtener una solicitud por ID
    @GetMapping("/{id}")
    public ResponseEntity<SolicitudEntity> obtenerSolicitud(@PathVariable Long id) {
        SolicitudEntity solicitud = solicitudService.obtenerSolicitudPorId(id);
        return solicitud != null ? ResponseEntity.ok(solicitud) : ResponseEntity.notFound().build();
    }

    // Endpoint para crear una nueva solicitud de préstamo
    @PostMapping("/crear")
    public ResponseEntity<String> crearSolicitud(
            @RequestParam String tipoPrestamo,
            @RequestParam double valorPropiedad,
            @RequestParam double montoPrestamo,
            @RequestParam double tasaInteresAnual,
            @RequestParam int plazo,
            @RequestBody List<DocumentoEntity> documentos) {

        try {
            solicitudService.crearSolicitud(tipoPrestamo, valorPropiedad, montoPrestamo, tasaInteresAnual, plazo, documentos);
            return ResponseEntity.ok("Solicitud de préstamo creada en revisión inicial.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
