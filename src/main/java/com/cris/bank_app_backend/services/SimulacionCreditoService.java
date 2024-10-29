package com.cris.bank_app_backend.services;

import org.springframework.stereotype.Service;

@Service
public class SimulacionCreditoService {

    public double calcularCuotaMensual(String tipoPrestamo, double valorPropiedad, double montoPrestamo, int plazo, double tasaInteresAnual) {
        // Verificación de restricciones según el tipo de préstamo
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

        // Cálculo de la cuota mensual
        double tasaInteresMensual = tasaInteresAnual / 12 / 100;
        int numeroTotalPagos = plazo * 12;
        return montoPrestamo * (tasaInteresMensual * Math.pow(1 + tasaInteresMensual, numeroTotalPagos)) /
                (Math.pow(1 + tasaInteresMensual, numeroTotalPagos) - 1);
    }
}
