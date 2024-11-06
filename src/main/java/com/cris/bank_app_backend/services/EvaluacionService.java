package com.cris.bank_app_backend.services;

import com.cris.bank_app_backend.entities.SolicitudEntity;
import com.cris.bank_app_backend.repositories.SolicitudRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class EvaluacionService {

    @Autowired
    private SolicitudRepository solicitudPrestamoRepository;

    @Autowired
    private PrestamoService prestamoService;

    public boolean evaluarSolicitud(SolicitudEntity solicitud, double ingresosMensuales, boolean buenHistorialCrediticio, int antiguedadLaboral, double totalDeudas, int edadCliente,
                                    double saldoCuenta, boolean saldoConsistente, double totalDepositos, int antiguedadCuenta, double porcentajeRetiroReciente) {
        List<String> razonesRechazo = new ArrayList<>();
        int reglasCapacidadAhorroCumplidas = 0;

        // Calcular cuota mensual del préstamo
        double cuotaMensual = prestamoService.calcularCuotaMensual(solicitud.getMontoDelPrestamo(), solicitud.getPlazo(), solicitud.getTasaDeInteresAnual());
        // R1: Relación Cuota/Ingreso
        double relacionCuotaIngreso = (cuotaMensual / ingresosMensuales) * 100;
        if (relacionCuotaIngreso > 35) {
            razonesRechazo.add("La relación cuota/ingreso supera el 35%");
        }

        // R2: Historial Crediticio del Cliente
        if (!buenHistorialCrediticio) {
            razonesRechazo.add("El historial crediticio es desfavorable");
        }

        // R3: Antigüedad Laboral y Estabilidad
        if (antiguedadLaboral < 2) {
            razonesRechazo.add("Antigüedad laboral insuficiente (menos de 2 años)");
        }

        // R4: Relación Deuda/Ingreso
        double relacionDeudaIngreso = ((totalDeudas + cuotaMensual) / ingresosMensuales) * 100;
        if (relacionDeudaIngreso > 50) {
            razonesRechazo.add("La relación deuda/ingreso supera el 50%");
        }

        // R6: Edad del Solicitante
        int edadAlTerminarPrestamo = edadCliente + solicitud.getPlazo();
        if (edadAlTerminarPrestamo > 75) {
            razonesRechazo.add("La edad del cliente al finalizar el préstamo superará los 75 años");
        }

        // R7: Capacidad de Ahorro
        if (saldoCuenta < 0.10 * solicitud.getMontoDelPrestamo()) {
            razonesRechazo.add("Saldo en cuenta menor al 10% del monto del préstamo solicitado");
        } else {
            reglasCapacidadAhorroCumplidas++;
        }

        if (!saldoConsistente) {
            razonesRechazo.add("El cliente no ha mantenido saldo positivo durante los últimos 12 meses");
        } else {
            reglasCapacidadAhorroCumplidas++;
        }

        if (totalDepositos < 0.05 * ingresosMensuales) {
            razonesRechazo.add("Depósitos no alcanzan el 5% de los ingresos mensuales");
        } else {
            reglasCapacidadAhorroCumplidas++;
        }

        if (antiguedadCuenta < 2) {
            if (saldoCuenta < 0.20 * solicitud.getMontoDelPrestamo()) {
                razonesRechazo.add("Saldo en cuenta menor al 20% del monto del préstamo solicitado con menos de 2 años en cuenta");
            } else {
                reglasCapacidadAhorroCumplidas++;
            }
        } else {
            if (saldoCuenta < 0.10 * solicitud.getMontoDelPrestamo()) {
                razonesRechazo.add("Saldo en cuenta menor al 10% del monto del préstamo solicitado con 2 años o más en cuenta");
            } else {
                reglasCapacidadAhorroCumplidas++;
            }
        }

        if (porcentajeRetiroReciente > 30) {
            razonesRechazo.add("Retiros recientes superiores al 30% del saldo de la cuenta");
        } else {
            reglasCapacidadAhorroCumplidas++;
        }

        // Evaluación de la capacidad de ahorro
        if (reglasCapacidadAhorroCumplidas >= 5) {
            solicitud.setCapacidadAhorro("sólida");
        } else if (reglasCapacidadAhorroCumplidas >= 3) {
            solicitud.setCapacidadAhorro("moderada: revisión adicional");
        } else {
            solicitud.setCapacidadAhorro("insuficiente");
            razonesRechazo.add("Capacidad de ahorro insuficiente");
        }

        // Actualizar solicitud según el resultado de la evaluación
        if (razonesRechazo.isEmpty()) {
            solicitud.setEstado("Pre-Aprobada");
            prestamoService.calcularPrestamo(solicitud);
        } else {
            solicitud.setEstado("Rechazada");
            solicitud.setRazonesRechazo(razonesRechazo);
        }

        solicitudPrestamoRepository.save(solicitud);
        return razonesRechazo.isEmpty();
    }
}
