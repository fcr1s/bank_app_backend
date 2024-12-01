package com.cris.bank_app_backend.services;

import com.cris.bank_app_backend.entities.PrestamoEntity;
import com.cris.bank_app_backend.repositories.PrestamoRepository;
import com.cris.bank_app_backend.entities.SolicitudEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PrestamoService {

    @Autowired
    private PrestamoRepository prestamoRepository;

    // Metodo para guardar un nuevo préstamo
    public PrestamoEntity guardarPrestamo(PrestamoEntity prestamo) {
        return prestamoRepository.save(prestamo);
    }

    // Metodo para obtener todos los préstamos
    public List<PrestamoEntity> obtenerTodosLosPrestamos() {
        return prestamoRepository.findAll();
    }

    // Metodo para obtener un préstamo por ID
    public PrestamoEntity obtenerPrestamoPorId(Long id) {
        return prestamoRepository.findById(id).orElse(null);
    }

    // Metodo para encontrar un préstamo por el ID de la solicitud
    public PrestamoEntity obtenerPrestamoPorSolicitudId(Long solicitudId) {
        return prestamoRepository.findBySolicitudId(solicitudId);
    }

    // Metodo para eliminar un préstamo por ID
    public void eliminarPrestamo(Long id) {
        prestamoRepository.deleteById(id);
    }

    // Metodo para calcular la cuota mensual de un préstamo
    public double calcularCuotaMensual(double montoPrestamo, int plazo, double tasaInteresAnual) {
        double tasaInteresMensual = tasaInteresAnual / 12 / 100;
        int numeroTotalPagos = plazo * 12;
        return montoPrestamo * (tasaInteresMensual * Math.pow(1 + tasaInteresMensual, numeroTotalPagos)) /
                (Math.pow(1 + tasaInteresMensual, numeroTotalPagos) - 1);
    }

    // Verificación de restricciones según el tipo de préstamo
    public void verificarRestricciones(String tipoPrestamo, double valorPropiedad, double montoPrestamo, int plazo, double tasaInteresAnual) {
        switch (tipoPrestamo.toLowerCase()) {
            case "primera vivienda":
                if (plazo > 30 || montoPrestamo > valorPropiedad * 0.8 || tasaInteresAnual < 3.5 || tasaInteresAnual > 5.0) {
                    throw new IllegalArgumentException("Parámetros inválidos para primera vivienda");
                }
                break;
            case "segunda vivienda":
                if (plazo > 20 || montoPrestamo > valorPropiedad * 0.7 || tasaInteresAnual < 4.0 || tasaInteresAnual > 6.0) {
                    throw new IllegalArgumentException("Parámetros inválidos para segunda vivienda");
                }
                break;
            case "propiedades comerciales":
                if (plazo > 25 || montoPrestamo > valorPropiedad * 0.6 || tasaInteresAnual < 5.0 || tasaInteresAnual > 7.0) {
                    throw new IllegalArgumentException("Parámetros inválidos para propiedades comerciales");
                }
                break;
            case "remodelacion":
                if (plazo > 15 || montoPrestamo > valorPropiedad * 0.5 || tasaInteresAnual < 4.5 || tasaInteresAnual > 6.0) {
                    throw new IllegalArgumentException("Parámetros inválidos para remodelación");
                }
                break;
            default:
                throw new IllegalArgumentException("Tipo de préstamo no válido");
        }
    }

    // Metodo para calcular costos del préstamo
    public void calcularPrestamo(SolicitudEntity solicitud) {
        double cuotaMensual = calcularCuotaMensual( solicitud.getMontoDelPrestamo(),
                solicitud.getPlazo(),
                solicitud.getTasaDeInteresAnual());

        double seguroDeDesgravamen = (0.03 / 100) * solicitud.getMontoDelPrestamo(); // 0.03% del monto del préstamo por mes
        double seguroDeIncendio = 20000; // Costo fijo
        double comisionPorAdministracion = (0.001) * solicitud.getMontoDelPrestamo(); // 1% del monto del préstamo

        double costoMensual = cuotaMensual + seguroDeDesgravamen + seguroDeIncendio;
        double costosTotales = (costoMensual * (solicitud.getPlazo() * 12)) + solicitud.getMontoDelPrestamo();

        PrestamoEntity prestamo = new PrestamoEntity();
        prestamo.setTipoPrestamo(solicitud.getTipoPrestamo());
        prestamo.setPlazo(solicitud.getPlazo());
        prestamo.setNumeroDeCuotas(solicitud.getPlazo() * 12); // Asumiendo que el plazo está en años
        prestamo.setMontoDelPrestamo(solicitud.getMontoDelPrestamo());
        prestamo.setTasaDeInteresAnual(solicitud.getTasaDeInteresAnual());
        prestamo.setTasaDeInteresMensual(solicitud.getTasaDeInteresAnual() / 12 / 100);
        prestamo.setCuotaMensual(cuotaMensual);
        prestamo.setSeguroDeDesgravamen(seguroDeDesgravamen);
        prestamo.setSeguroDeIncendio(seguroDeIncendio);
        prestamo.setComisionPorAdministracion(comisionPorAdministracion);
        prestamo.setCostoMensual(costoMensual);
        prestamo.setCostosTotales(costosTotales);
        prestamo.setSolicitudId(solicitud.getId());

        prestamoRepository.save(prestamo);

    }
}
