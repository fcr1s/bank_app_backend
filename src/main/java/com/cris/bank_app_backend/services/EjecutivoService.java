package com.cris.bank_app_backend.services;

import com.cris.bank_app_backend.entities.ClienteEntity;
import com.cris.bank_app_backend.entities.EjecutivoEntity;
import com.cris.bank_app_backend.entities.SolicitudEntity;
import com.cris.bank_app_backend.repositories.EjecutivoRepository;
import com.cris.bank_app_backend.repositories.SolicitudRepository;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.NoSuchElementException;


@Service
public class EjecutivoService {

    @Autowired
    private EjecutivoRepository ejecutivoRepository;

    @Autowired
    private SolicitudRepository solicitudRepository;

    private static EjecutivoEntity ejecutivoLogueado;

    // Método para iniciar sesión
    public EjecutivoEntity login(String rut, String password) {
        ejecutivoLogueado = ejecutivoRepository.findByRutAndPassword(rut, password).orElse(null);
        return ejecutivoLogueado;
    }

    public void logout() {
        ejecutivoLogueado = null; // Limpiar la sesión
    }

    // Método para obtener el cliente logueado
    public EjecutivoEntity obtenerClienteLogueado() {
        if (ejecutivoLogueado == null) {
            throw new IllegalStateException("No hay un ejecutivo logueado.");
        }
        return ejecutivoLogueado;
    }

    // Método para verificar si hay un ejecutivo logueado
    public boolean estaLogueado() {
        return ejecutivoLogueado != null;
    }

    // Método para obtener todas las solicitudes
    public List<SolicitudEntity> obtenerTodasLasSolicitudes() {
        return solicitudRepository.findAll();
    }

    // Método para obtener solicitudes por estado
    public List<SolicitudEntity> obtenerSolicitudesPorEstado(String estado) {
        // Validamos que el estado sea uno de los permitidos
        List<String> estadosPermitidos = List.of(
                "En revisión inicial", "Pendiente de documentación", "En evaluación",
                "Pre-Aprobada", "En aprobación final", "Aprobada"
        );

        if (!estadosPermitidos.contains(estado)) {
            throw new IllegalArgumentException("Estado no válido");
        }

        return solicitudRepository.findByEstado(estado);
    }

    // Método para actualizar el estado de una solicitud
    public void actualizarEstadoSolicitud(Long id, String nuevoEstado) {
        // Validar que el nuevo estado sea uno de los permitidos
        List<String> estadosPermitidos = List.of(
                "En revisión inicial", "Pendiente de documentación", "En evaluación",
                "Pre-Aprobada", "En aprobación final", "Aprobada", "Rechazada"
        );

        if (!estadosPermitidos.contains(nuevoEstado)) {
            throw new IllegalArgumentException("Estado no válido");
        }

        SolicitudEntity solicitud = solicitudRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Solicitud no encontrada"));

        solicitud.setEstado(nuevoEstado);
        solicitudRepository.save(solicitud);
    }
}
