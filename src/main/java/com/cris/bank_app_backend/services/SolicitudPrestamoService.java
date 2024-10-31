package com.cris.bank_app_backend.services;

import com.cris.bank_app_backend.entities.DocumentosEntity;
import com.cris.bank_app_backend.repositories.DocumentosRepository;
import com.cris.bank_app_backend.entities.SolicitudPrestamoEntity;
import com.cris.bank_app_backend.repositories.SolicitudPrestamoRepository;
import com.cris.bank_app_backend.repositories.ClienteRepository;
import com.cris.bank_app_backend.entities.ClienteEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class SolicitudPrestamoService {

    @Autowired
    private SolicitudPrestamoRepository solicitudPrestamoRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private DocumentosRepository documentosRepository;

    @Autowired
    private SimulacionCreditoService simulacionCreditoService;

    /**
     * Crear una nueva solicitud de préstamo
     */
    public SolicitudPrestamoEntity iniciarSolicitud(String rut, String tipoPrestamo, double valorPropiedad,
                                                    double montoPrestamo, int plazo, double tasaInteresAnual,
                                                    DocumentosEntity documentos) {
        // Buscar el cliente por su rut
        ClienteEntity cliente = clienteRepository.findByRut(rut);
        // Validar los datos de simulación de crédito
        double cuotaMensual = simulacionCreditoService.calcularCuotaMensual(
                tipoPrestamo, valorPropiedad, montoPrestamo, plazo, tasaInteresAnual);

        // Verificar los documentos según el tipo de préstamo
        validarDocumentos(tipoPrestamo, documentos);

        // Guardar los documentos en la base de datos
        DocumentosEntity documentosGuardados = documentosRepository.save(documentos);

        // Crear la solicitud de préstamo con estado "Revisión inicial"
        SolicitudPrestamoEntity solicitud = new SolicitudPrestamoEntity();
        solicitud.setCliente(cliente);
        solicitud.setEstado("Revisión inicial");
        solicitud.setFechaDeSolicitud(new Date());
        solicitud.setDocumentos(documentosGuardados);

        return solicitudPrestamoRepository.save(solicitud);
    }

    /**
     * Validar que los documentos subidos cumplan con los requisitos del tipo de préstamo
     */
    private void validarDocumentos(String tipoPrestamo, DocumentosEntity documentos) {
        switch (tipoPrestamo.toLowerCase()) {
            case "primera vivienda":
                if (documentos.getComprobanteIngresos() == null || documentos.getCertificadoDeAvaluo() == null ||
                        documentos.getHitorialCrediticio() == null) {
                    throw new IllegalArgumentException("Faltan documentos para primera vivienda");
                }
                break;
            case "segunda vivienda":
                if (documentos.getComprobanteIngresos() == null || documentos.getCertificadoDeAvaluo() == null ||
                        documentos.getHitorialCrediticio() == null || documentos.getEscrituraDePropiedad() == null) {
                    throw new IllegalArgumentException("Faltan documentos para segunda vivienda");
                }
                break;
            case "propiedades comerciales":
                if (documentos.getComprobanteIngresos() == null || documentos.getCertificadoDeAvaluo() == null ||
                        documentos.getPlanDeNegocio() == null || documentos.getEstadoFinancieroDelNegocio() == null) {
                    throw new IllegalArgumentException("Faltan documentos para propiedades comerciales");
                }
                break;
            case "remodelacion":
                if (documentos.getComprobanteIngresos() == null || documentos.getCertificadoDeAvaluo() == null ||
                        documentos.getPresupuestoDeLaRemodelacion() == null) {
                    throw new IllegalArgumentException("Faltan documentos para remodelación");
                }
                break;
            default:
                throw new IllegalArgumentException("Tipo de préstamo no válido");
        }
    }

    // Método para cambiar el estado de la solicitud y registrar documentos pendientes
    public SolicitudPrestamoEntity marcarPendienteDocumentacion(Long solicitudId, List<String> documentosPendientes) {
        SolicitudPrestamoEntity solicitud = solicitudPrestamoRepository.findById(solicitudId)
                .orElseThrow(() -> new IllegalArgumentException("Solicitud no encontrada"));

        if (documentosPendientes != null && !documentosPendientes.isEmpty()) {
            solicitud.setEstado("Pendiente de documentación");
            solicitud.setDocumentosPendientes(documentosPendientes);
        } else {
            solicitud.setEstado("En evaluación");
            solicitud.setDocumentosPendientes(null); // Limpiamos la lista si ya no hay documentos pendientes
        }

        return solicitudPrestamoRepository.save(solicitud);
    }
}

