package com.cris.bank_app_backend.controllers;

import com.cris.bank_app_backend.entities.DocumentoEntity;

import com.cris.bank_app_backend.services.DocumentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/documentos")
@CrossOrigin("*")
public class DocumentoController {

    @Autowired
    private DocumentoService documentoService;

    // Endpoint para guardar un nuevo documento
    @PostMapping("/guardar")
    public ResponseEntity<DocumentoEntity> guardarDocumento(@RequestBody DocumentoEntity documento) {
        return ResponseEntity.ok(documentoService.guardarDocumento(documento));
    }

    // Endpoint para obtener documentos por rut de cliente
    @GetMapping("/cliente/{rutCliente}")
    public ResponseEntity<List<DocumentoEntity>> obtenerDocumentosPorCliente(@PathVariable String rutCliente) {
        return ResponseEntity.ok(documentoService.obtenerDocumentosPorCliente(rutCliente));
    }
}



