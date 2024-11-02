package com.cris.bank_app_backend.services;

import com.cris.bank_app_backend.entities.EjecutivoEntity;
import com.cris.bank_app_backend.entities.SolicitudEntity;
import com.cris.bank_app_backend.repositories.EjecutivoRepository;
import com.cris.bank_app_backend.repositories.SolicitudRepository;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;


@Service
public class EjecutivoService {

    @Autowired
    private EjecutivoRepository ejecutivoRepository;

    @Autowired
    private SolicitudRepository solicitudRepository;

    // Método para iniciar sesión
    public EjecutivoEntity login(String rut, String password) {
        return ejecutivoRepository.findByRutAndPassword(rut, password).orElse(null);
    }

    // Método para actualizar el estado de una solicitud
    public void actualizarEstadoSolicitud(Long solicitudId, String nuevoEstado) {
        SolicitudEntity solicitud = solicitudRepository.findById(solicitudId).orElse(null);
        if (solicitud != null) {
            solicitud.setEstado(nuevoEstado);
            solicitudRepository.save(solicitud);
        }
    }
}
