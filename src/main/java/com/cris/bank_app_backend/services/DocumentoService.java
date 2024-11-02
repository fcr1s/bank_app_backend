package com.cris.bank_app_backend.services;

import com.cris.bank_app_backend.entities.DocumentoEntity;
import com.cris.bank_app_backend.repositories.DocumentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DocumentoService {

    @Autowired
    private DocumentoRepository documentoRepository;

    // Método para guardar un nuevo documento
    public DocumentoEntity guardarDocumento(DocumentoEntity documento) {
        return documentoRepository.save(documento);
    }

    // Método para obtener documentos por rut de cliente
    public List<DocumentoEntity> obtenerDocumentosPorCliente(String rutCliente) {
        return documentoRepository.findByRutCliente(rutCliente);
    }

    public boolean validarDocumentos(String tipoPrestamo, List<DocumentoEntity> documentos) {
        List<String> documentosRequeridos;

        // Definir los documentos requeridos según el tipo de préstamo
        switch (tipoPrestamo) {
            case "primera vivienda":
                documentosRequeridos = List.of("comprobante de ingresos", "certificado de avaluo", "historial crediticio");
                break;
            case "segunda vivienda":
                documentosRequeridos = List.of("comprobante de ingresos", "certificado de avaluo", "historial crediticio", "escritura primera vivienda");
                break;
            case "propiedades comerciales":
                documentosRequeridos = List.of("comprobante de ingresos", "certificado de avaluo", "estado financiero del negocio", "plan de negocios");
                break;
            case "remodelacion":
                documentosRequeridos = List.of("comprobante de ingresos", "certificado de avaluo", "presupuesto de remodelacion");
                break;
            default:
                throw new IllegalArgumentException("Tipo de préstamo no reconocido.");
        }

        // Verificar que todos los documentos requeridos estén presentes
        List<String> tiposDeDocumentos = documentos.stream().map(DocumentoEntity::getTipoDeDocumento).toList();
        return tiposDeDocumentos.containsAll(documentosRequeridos);
    }
}

