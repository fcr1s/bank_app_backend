package com.cris.bank_app_backend.services;

import com.cris.bank_app_backend.entities.DocumentoEntity;
import com.cris.bank_app_backend.repositories.DocumentoRepository;
import com.cris.bank_app_backend.entities.SolicitudEntity;
import com.cris.bank_app_backend.repositories.SolicitudRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class DocumentoService {

    @Autowired
    private DocumentoRepository documentoRepository;

    @Autowired
    private SolicitudRepository solicitudRepository;

    // Método para guardar un nuevo documento
    public DocumentoEntity guardarDocumento(DocumentoEntity documento) {
        return documentoRepository.save(documento);
    }

    // Método para obtener documentos por rut de cliente
    public List<DocumentoEntity> obtenerDocumentosPorCliente(String rutCliente) {
        return documentoRepository.findByRutCliente(rutCliente);
    }

    // Método para obtener documentos por solicitudId
    public List<DocumentoEntity> obtenerDocumentosPorSolicitudId(Long solicitudId) {
        return documentoRepository.findBySolicitudId(solicitudId);
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

    public void actualizarDocumentos(Long solicitudId, List<DocumentoEntity> nuevosDocumentos) {
        // Verificar que la solicitud esté en estado "Pendiente de documentación"
        SolicitudEntity solicitud = solicitudRepository.findById(solicitudId)
                .orElseThrow(() -> new NoSuchElementException("Solicitud no encontrada para el ID: " + solicitudId));

        if (!solicitud.getEstado().equals("Pendiente de documentación")) {
            throw new IllegalArgumentException("La solicitud no está en estado 'Pendiente de documentación'");
        }

        // Eliminar documentos existentes asociados a la solicitud
        List<DocumentoEntity> documentosExistentes = documentoRepository.findBySolicitudId(solicitudId);
        documentoRepository.deleteAll(documentosExistentes);

        // Guardar los nuevos documentos
        for (DocumentoEntity documento : nuevosDocumentos) {
            documento.setSolicitudId(solicitudId); // Asociar el documento con el ID de la solicitud
            documentoRepository.save(documento);
        }

        // Cambiar el estado de la solicitud a "En revisión inicial"
        solicitud.setEstado("En revisión inicial");
        solicitudRepository.save(solicitud);
    }
}

