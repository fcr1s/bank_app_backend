package com.cris.bank_app_backend.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

import com.cris.bank_app_backend.entities.SolicitudEntity;
import com.cris.bank_app_backend.repositories.SolicitudRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;

public class EvaluacionServiceTest {

    @Mock
    private SolicitudRepository solicitudPrestamoRepository;

    @Mock
    private PrestamoService prestamoService;

    @InjectMocks
    private EvaluacionService evaluacionService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void evaluarSolicitud_WhenAllConditionsMet_ShouldReturnTrueAndSetPreApproved() {
        // Arrange
        SolicitudEntity solicitud = new SolicitudEntity();
        solicitud.setMontoDelPrestamo(100000);
        solicitud.setPlazo(12);
        solicitud.setTasaDeInteresAnual(5.0);

        double ingresosMensuales = 300000;
        boolean buenHistorialCrediticio = true;
        int antiguedadLaboral = 3; // 3 years
        double totalDeudas = 50000;
        double valorPropiedad = 150000;
        int edadCliente = 30; // 30 years old
        double saldoCuenta = 20000;
        boolean saldoConsistente = true;
        double totalDepositos = 40000;
        int antiguedadCuenta = 3; // 3 years
        double porcentajeRetiroReciente = 20; // 20%

        // Mock del cálculo de cuota mensual
        double cuotaMensual = 8575; // Expected monthly payment
        given(prestamoService.calcularCuotaMensual(solicitud.getMontoDelPrestamo(), solicitud.getPlazo(), solicitud.getTasaDeInteresAnual()))
                .willReturn(cuotaMensual);

        // Act
        boolean resultado = evaluacionService.evaluarSolicitud(solicitud, ingresosMensuales, buenHistorialCrediticio,
                antiguedadLaboral, totalDeudas, valorPropiedad, edadCliente,
                saldoCuenta, saldoConsistente, totalDepositos, antiguedadCuenta, porcentajeRetiroReciente);

        // Assert
        assertTrue(resultado);
        assertEquals("Pre-Aprobada", solicitud.getEstado());
        assertNull(solicitud.getRazonesRechazo());
        verify(solicitudPrestamoRepository, times(1)).save(solicitud);
    }

    @Test
    public void evaluarSolicitud_WhenDebtToIncomeRatioTooHigh_ShouldReturnFalseAndSetRejected() {
        // Arrange
        SolicitudEntity solicitud = new SolicitudEntity();
        solicitud.setMontoDelPrestamo(100000);
        solicitud.setPlazo(12);
        solicitud.setTasaDeInteresAnual(5.0);

        double ingresosMensuales = 100000; // Monthly income too low
        boolean buenHistorialCrediticio = true;
        int antiguedadLaboral = 3; // 3 years
        double totalDeudas = 70000; // High debts
        double valorPropiedad = 150000;
        int edadCliente = 30;
        double saldoCuenta = 20000;
        boolean saldoConsistente = true;
        double totalDepositos = 30000;
        int antiguedadCuenta = 3;
        double porcentajeRetiroReciente = 20;

        double cuotaMensual = 8575; // Expected monthly payment
        given(prestamoService.calcularCuotaMensual(solicitud.getMontoDelPrestamo(), solicitud.getPlazo(), solicitud.getTasaDeInteresAnual()))
                .willReturn(cuotaMensual);

        // Act
        boolean resultado = evaluacionService.evaluarSolicitud(solicitud, ingresosMensuales, buenHistorialCrediticio,
                antiguedadLaboral, totalDeudas, valorPropiedad, edadCliente,
                saldoCuenta, saldoConsistente, totalDepositos, antiguedadCuenta, porcentajeRetiroReciente);

        // Assert
        assertFalse(resultado);
        assertEquals("Rechazada", solicitud.getEstado());
        assertEquals(Arrays.asList("La relación deuda/ingreso supera el 50%"), solicitud.getRazonesRechazo());
        verify(solicitudPrestamoRepository, times(1)).save(solicitud);
    }


}
