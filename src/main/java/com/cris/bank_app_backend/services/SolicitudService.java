package com.cris.bank_app_backend.services;

import com.cris.bank_app_backend.entities.ClienteEntity;
import com.cris.bank_app_backend.entities.SolicitudEntity;
import com.cris.bank_app_backend.repositories.SolicitudRepository;
import com.cris.bank_app_backend.entities.DocumentoEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
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

    // Método para obtener todas las solicitudes
    public List<SolicitudEntity> obtenerSolicitudes() {
        return solicitudRepository.findAll();
    }

    // Método para obtener una solicitud por ID
    public SolicitudEntity obtenerSolicitudPorId(Long id) {
        return solicitudRepository.findById(id).orElse(null);
    }

    public void crearSolicitud(String tipoPrestamo, double valorPropiedad ,double montoPrestamo, double tasaInteresAnual, int plazo, List<DocumentoEntity> documentos) {

        // Obtener el cliente logueado
        ClienteEntity clienteLogueado = clienteService.obtenerClienteLogueado();

        // Verificar restricciones del préstamo
        prestamoService.verificarRestricciones(tipoPrestamo, valorPropiedad, montoPrestamo, plazo, tasaInteresAnual);

        // Validar documentos según el tipo de préstamo
        if (!documentoService.validarDocumentos(tipoPrestamo, documentos)) {
            throw new IllegalArgumentException("Documentos requeridos faltantes o incorrectos para el tipo de préstamo.");
        }

        // Crear la solicitud
        SolicitudEntity solicitud = new SolicitudEntity();
        solicitud.setRut(clienteLogueado.getRut());
        solicitud.setFechaDeSolicitud(new Date());
        solicitud.setEstado("En revisión inicial");
        solicitud.setTipoPrestamo(tipoPrestamo);
        solicitud.setMontoDelPrestamo(montoPrestamo);
        solicitud.setTasaDeInteresAnual(tasaInteresAnual);
        solicitud.setPlazo(plazo);
        solicitud.setClienteId(clienteLogueado.getId());

        // Guardar la solicitud
        solicitudRepository.save(solicitud);
    }


}

