package com.cris.bank_app_backend.controllers;

import com.cris.bank_app_backend.entities.DocumentoEntity;

import com.cris.bank_app_backend.services.DocumentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;


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

    // Endpoint para obtener documentos por rut de cliente con RequestParam
    @GetMapping("/obtener")
    public ResponseEntity<List<DocumentoEntity>> obtenerDocumentosPorRutCliente(
            @RequestParam String rutCliente) {
        return ResponseEntity.ok(documentoService.obtenerDocumentosPorCliente(rutCliente));
    }

    // Endpoint para obtener documentos por solicitudId con PathVariable
    @GetMapping("/obtener/{solicitudId}")
    public ResponseEntity<List<DocumentoEntity>> obtenerDocumentosPorSolicitudId(
            @PathVariable Long solicitudId) {
        return ResponseEntity.ok(documentoService.obtenerDocumentosPorSolicitudId(solicitudId));
    }

    // Endpoint para que el cliente actualice documentos
    @PutMapping("/actualizar")
    public ResponseEntity<String> actualizarDocumentos(
            @RequestParam Long solicitudId,
            @RequestBody List<DocumentoEntity> nuevosDocumentos) {
        try {
            documentoService.actualizarDocumentos(solicitudId, nuevosDocumentos);
            return ResponseEntity.ok("Documentos actualizados exitosamente. La solicitud vuelve a 'En revisión inicial'.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

}



