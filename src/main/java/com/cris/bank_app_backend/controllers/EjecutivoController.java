package com.cris.bank_app_backend.controllers;


import com.cris.bank_app_backend.entities.EjecutivoEntity;
import com.cris.bank_app_backend.entities.SolicitudEntity;
import com.cris.bank_app_backend.repositories.SolicitudRepository;
import com.cris.bank_app_backend.services.EvaluacionService;
import com.cris.bank_app_backend.services.EjecutivoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;


@RestController
@RequestMapping("/ejecutivos")
@CrossOrigin("http://4.228.227.122:8080")
public class EjecutivoController {

    @Autowired
    private EjecutivoService ejecutivoService;

    @Autowired
    private SolicitudRepository solicitudRepository;

    @Autowired
    private EvaluacionService evaluacionService;

    // Endpoint para registrar un nuevo ejecutivo
    @PostMapping("/registrarse")
    public ResponseEntity<EjecutivoEntity> registrarEjecutivo(@RequestBody EjecutivoEntity ejecutivo) {
        try {
            EjecutivoEntity nuevoEjecutivo = ejecutivoService.registrarEjecutivo(ejecutivo);
            return ResponseEntity.ok(nuevoEjecutivo);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        }
    }

    // Endpoint para iniciar sesión del ejecutivo
    @PostMapping("/login")
    public ResponseEntity<EjecutivoEntity> login(@RequestParam String rut, @RequestParam String password) {
        EjecutivoEntity ejecutivo = ejecutivoService.login(rut, password);
        return ejecutivo != null ? ResponseEntity.ok(ejecutivo) : ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    // Endpoint para que el ejecutivo consulte todas las solicitudes o filtre por estado
    @GetMapping("/solicitudes")
    public ResponseEntity<List<SolicitudEntity>> obtenerSolicitudes(
            @RequestParam(required = false) String estado) {
        List<SolicitudEntity> solicitudes;
        if (estado != null && !estado.isEmpty()) {
            solicitudes = ejecutivoService.obtenerSolicitudesPorEstado(estado);
        } else {
            solicitudes = ejecutivoService.obtenerTodasLasSolicitudes();
        }
        return ResponseEntity.ok(solicitudes);
    }

    // Endpoint para actualizar el estado de una solicitud
    @PutMapping("/solicitudes/{id}/estado")
    public ResponseEntity<String> actualizarEstadoSolicitud(
            @PathVariable Long id, @RequestParam String nuevoEstado) {
        try {
            ejecutivoService.actualizarEstadoSolicitud(id, nuevoEstado);
            return ResponseEntity.ok("Estado actualizado exitosamente.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PutMapping("/evaluar-solicitud/{solicitudId}")
    public ResponseEntity<String> evaluarSolicitud(@PathVariable Long solicitudId, @RequestParam double ingresosMensuales,@RequestParam boolean buenHistorialCrediticio, @RequestParam int antiguedadLaboral,
                                                   @RequestParam double totalDeudas, @RequestParam int edadCliente,
                                                   @RequestParam double saldoCuenta, @RequestParam boolean saldoConsistente, @RequestParam double totalDepositos,
                                                   @RequestParam int antiguedadCuenta, @RequestParam double porcentajeRetiroReciente) {
        Optional<SolicitudEntity> solicitudOpt = solicitudRepository.findById(solicitudId);
        if (solicitudOpt.isPresent()) {
            SolicitudEntity solicitud = solicitudOpt.get();
            boolean esAprobada = evaluacionService.evaluarSolicitud(solicitud, ingresosMensuales, buenHistorialCrediticio, antiguedadLaboral, totalDeudas, edadCliente,
                    saldoCuenta, saldoConsistente, totalDepositos, antiguedadCuenta, porcentajeRetiroReciente);

            if (esAprobada) {
                return ResponseEntity.ok("Solicitud Pre-Aprobada");
            } else {
                return ResponseEntity.ok("Solicitud Rechazada por las siguientes razones: " + solicitud.getRazonesRechazo());
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Solicitud no encontrada");
    }

}


