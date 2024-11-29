package com.cris.bank_app_backend.services;

import com.cris.bank_app_backend.entities.DocumentoEntity;
import com.cris.bank_app_backend.repositories.DocumentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class DocumentoService {

    @Autowired
    private DocumentoRepository documentoRepository;


    // Metodo para obtener un documento por ID
    public DocumentoEntity obtenerDocumentoPorId(Long id) {
        return documentoRepository.findById(id).orElse(null);
    }

    // Metodo para obtener documentos por solicitudId
    public List<DocumentoEntity> obtenerDocumentosPorSolicitudId(Long solicitudId) {
        return documentoRepository.findBySolicitudId(solicitudId);
    }

    public void saveDocument(DocumentoEntity documento) {
        documentoRepository.save(documento);
    }

    // Metodo para guardar un documento
    public void guardarDocumento(MultipartFile archivo, DocumentoEntity documento) {
        try {
            // Convierte el archivo a un arreglo de bytes
            byte[] bytes = archivo.getBytes();
            documento.setDocumento(bytes); // Almacena el documento en el arreglo de bytes

            // Guarda el documento en la base de datos
            documentoRepository.save(documento);
        } catch (IOException e) {
            throw new RuntimeException("Error al guardar el documento: " + e.getMessage());
        }
    }
}

