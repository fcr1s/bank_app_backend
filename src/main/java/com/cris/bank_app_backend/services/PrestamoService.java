package com.cris.bank_app_backend.services;

import com.cris.bank_app_backend.entities.PrestamoEntity;
import com.cris.bank_app_backend.repositories.PrestamoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PrestamoService {

    @Autowired
    private PrestamoRepository prestamoRepository;

    // Método para guardar un nuevo préstamo
    public PrestamoEntity guardarPrestamo(PrestamoEntity prestamo) {
        return prestamoRepository.save(prestamo);
    }

    // Método para obtener todos los préstamos
    public List<PrestamoEntity> obtenerTodosLosPrestamos() {
        return prestamoRepository.findAll();
    }

    // Método para calcular la cuota mensual de un préstamo
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

}
