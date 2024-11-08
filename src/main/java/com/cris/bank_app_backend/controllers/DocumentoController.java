package com.cris.bank_app_backend.controllers;

import com.cris.bank_app_backend.entities.DocumentoEntity;

import com.cris.bank_app_backend.services.DocumentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;



@RestController
@RequestMapping("/documentos")
@CrossOrigin
public class DocumentoController {

    @Autowired
    private DocumentoService documentoService;

    @PostMapping("/guardar")
    public ResponseEntity<String> guardarDocumento(
            @RequestParam Long solicitudId,
            @RequestParam("documento") MultipartFile archivo) { // "documento" no es nece

        try {
            DocumentoEntity documento = new DocumentoEntity();
            documento.setSolicitudId(solicitudId);

            // Guarda el archivo en el documento
            documentoService.guardarDocumento(archivo, documento);

            return ResponseEntity.ok("Documento guardado con éxito.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al guardar el documento: " + e.getMessage());
        }
    }


    // Endpoint para obtener documentos por solicitudId con PathVariable
    @GetMapping("/obtener/{solicitudId}")
    public ResponseEntity<List<DocumentoEntity>> obtenerDocumentosPorSolicitudId(
            @PathVariable Long solicitudId) {
        return ResponseEntity.ok(documentoService.obtenerDocumentosPorSolicitudId(solicitudId));
    }

    // Endpoint para obtener un documento por ID
    @GetMapping("/descargar/{id}")
    public ResponseEntity<byte[]> descargarDocumento(@PathVariable Long id) {
        try {
            DocumentoEntity documento = documentoService.obtenerDocumentoPorId(id);
            if (documento == null || documento.getDocumento() == null) {
                return ResponseEntity.notFound().build(); // Si no se encuentra el documento
            }

            // Establece el tipo de contenido según el tipo de documento
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDisposition(ContentDisposition.attachment().filename("documento.pdf").build()); // Cambia el nombre según sea necesario

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(documento.getDocumento()); // Devuelve el contenido del documento
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }

    }
}



