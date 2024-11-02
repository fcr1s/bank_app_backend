package com.cris.bank_app_backend.controllers;

import com.cris.bank_app_backend.entities.EjecutivoEntity;
import com.cris.bank_app_backend.services.EjecutivoService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


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

    // Endpoint para actualizar el estado de una solicitud
    @PutMapping("/actualizar-estado/{solicitudId}")
    public ResponseEntity<Void> actualizarEstado(@PathVariable Long solicitudId, @RequestParam String nuevoEstado) {
        ejecutivoService.actualizarEstadoSolicitud(solicitudId, nuevoEstado);
        return ResponseEntity.ok().build();
    }
}


