package com.cris.bank_app_backend.controllers;

import org.springframework.http.ResponseEntity;

import com.cris.bank_app_backend.entities.PrestamoEntity;
import com.cris.bank_app_backend.services.PrestamoService;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@RestController
@RequestMapping("/prestamos")
@CrossOrigin("*")
public class PrestamoController {

    @Autowired
    private PrestamoService prestamoService;

    // Endpoint para guardar un nuevo préstamo
    @PostMapping("/guardar")
    public ResponseEntity<PrestamoEntity> guardarPrestamo(@RequestBody PrestamoEntity prestamo) {
        return ResponseEntity.ok(prestamoService.guardarPrestamo(prestamo));
    }

    // Endpoint para obtener todos los préstamos
    @GetMapping("/todos")
    public ResponseEntity<List<PrestamoEntity>> obtenerTodosLosPrestamos() {
        return ResponseEntity.ok(prestamoService.obtenerTodosLosPrestamos());
    }
}
