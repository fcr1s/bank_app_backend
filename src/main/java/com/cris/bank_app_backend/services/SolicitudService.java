package com.cris.bank_app_backend.services;

import com.cris.bank_app_backend.entities.ClienteEntity;
import com.cris.bank_app_backend.entities.SolicitudEntity;
import com.cris.bank_app_backend.repositories.SolicitudRepository;
import com.cris.bank_app_backend.entities.DocumentoEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.NoSuchElementException;

import java.util.List;

@Service
public class SolicitudService {

    @Autowired
    private SolicitudRepository solicitudRepository;

    @Autowired
    private DocumentoService documentoService;

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private PrestamoService prestamoService;

    // Metodo para obtener todas las solicitudes
    public List<SolicitudEntity> obtenerSolicitudes() {
        return solicitudRepository.findAll();
    }

    // Metodo para obtener una solicitud por ID
    public SolicitudEntity obtenerSolicitudPorId(Long id) {
        return solicitudRepository.findById(id).orElse(null);
    }

    // Metodo para eliminar una solicitud por ID
    public void eliminarSolicitud(Long id) {
        solicitudRepository.deleteById(id);
    }

    public void crearSolicitud(String tipoPrestamo, double valorPropiedad,
                               double montoPrestamo, double tasaInteresAnual, int plazo,
                               List<MultipartFile> documentos) {

        // Obtener el cliente logueado
        ClienteEntity clienteLogueado = clienteService.obtenerClienteLogueado();

        // Verificar restricciones del préstamo
        prestamoService.verificarRestricciones(tipoPrestamo, valorPropiedad, montoPrestamo, plazo, tasaInteresAnual);

        // Crear la solicitud
        SolicitudEntity solicitud = new SolicitudEntity();
        solicitud.setRut(clienteLogueado.getRut());
        solicitud.setEstado("En revisión inicial");
        solicitud.setTipoPrestamo(tipoPrestamo);
        solicitud.setMontoDelPrestamo(montoPrestamo);
        solicitud.setTasaDeInteresAnual(tasaInteresAnual);
        solicitud.setPlazo(plazo);
        solicitud.setClienteId(clienteLogueado.getId());

        // Guardar la solicitud
        solicitudRepository.save(solicitud);

        // Guardar los documentos
        for (MultipartFile file : documentos) {
            // Crear un nuevo DocumentoEntity para cada archivo
            DocumentoEntity documento = new DocumentoEntity();
            documento.setSolicitudId(solicitud.getId());

            // Guardar el archivo en la base de datos
            try {
                documento.setDocumento(file.getBytes());
                documentoService.saveDocument(documento);
            } catch (IOException e) {
                throw new IllegalArgumentException("Error al guardar el documento: " + e.getMessage());
            }
        }
    }


    public List<SolicitudEntity> obtenerSolicitudesDelCliente() {
        // Obtener el cliente logueado
        ClienteEntity clienteLogueado = clienteService.obtenerClienteLogueado();
        // Consultar solicitudes del cliente
        return solicitudRepository.findByClienteId(clienteLogueado.getId());
    }

    public void cancelarSolicitud(Long solicitudId) {
        // Buscar la solicitud por ID y verificar que pertenece al cliente indicado
        SolicitudEntity solicitud = solicitudRepository.findById(solicitudId)
                .orElseThrow(() -> new NoSuchElementException("Solicitud no encontrada para el ID: " + solicitudId));
        // Verificar si el estado de la solicitud permite la cancelación
        if (!solicitud.getEstado().equals("En revisión inicial") &&
                !solicitud.getEstado().equals("Pendiente de documentación") &&
                !solicitud.getEstado().equals("En evaluación") &&
                !solicitud.getEstado().equals("Pre-aprobada")) {
            throw new IllegalArgumentException("La solicitud no puede ser cancelada en su estado actual.");
        }

        // Actualizar el estado de la solicitud a "Cancelada por el cliente"
        solicitud.setEstado("Cancelada por el cliente");
        solicitudRepository.save(solicitud);
    }

    // Metodo para actualizar el estado de una solicitud
    public void actualizarEstadoSolicitud(Long solicitudId, String nuevoEstado) {
        SolicitudEntity solicitud = solicitudRepository.findById(solicitudId)
                .orElseThrow(() -> new NoSuchElementException("Solicitud no encontrada para el ID: " + solicitudId));
        solicitud.setEstado(nuevoEstado);
        solicitudRepository.save(solicitud);
    }

}



