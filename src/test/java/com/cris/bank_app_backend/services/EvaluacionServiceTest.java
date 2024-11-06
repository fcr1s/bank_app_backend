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

import java.util.List;

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

        SolicitudEntity solicitud = new SolicitudEntity();
        solicitud.setMontoDelPrestamo(100000);
        solicitud.setPlazo(12);
        solicitud.setTasaDeInteresAnual(5.0);

        double ingresosMensuales = 300000;
        boolean buenHistorialCrediticio = true;
        int antiguedadLaboral = 3;
        double totalDeudas = 50000;

        int edadCliente = 30;
        double saldoCuenta = 20000;
        boolean saldoConsistente = true;
        double totalDepositos = 40000;
        int antiguedadCuenta = 3;
        double porcentajeRetiroReciente = 20;

        // Mock del cálculo de cuota mensual
        double cuotaMensual = 8575; // Expected monthly payment
        given(prestamoService.calcularCuotaMensual(solicitud.getMontoDelPrestamo(), solicitud.getPlazo(), solicitud.getTasaDeInteresAnual()))
                .willReturn(cuotaMensual);


        boolean resultado = evaluacionService.evaluarSolicitud(solicitud, ingresosMensuales, buenHistorialCrediticio,
                antiguedadLaboral, totalDeudas, edadCliente,
                saldoCuenta, saldoConsistente, totalDepositos, antiguedadCuenta, porcentajeRetiroReciente);


        assertTrue(resultado);
        assertEquals("Pre-Aprobada", solicitud.getEstado());

        assertTrue(solicitud.getRazonesRechazo().isEmpty());
        verify(solicitudPrestamoRepository, times(1)).save(solicitud);
    }

    @Test
    public void evaluarSolicitud_WhenDebtToIncomeRatioTooHigh_ShouldReturnFalseAndSetRejected() {

        SolicitudEntity solicitud = new SolicitudEntity();
        solicitud.setMontoDelPrestamo(100000);
        solicitud.setPlazo(12);
        solicitud.setTasaDeInteresAnual(5.0);

        double ingresosMensuales = 100000;
        boolean buenHistorialCrediticio = true;
        int antiguedadLaboral = 3;
        double totalDeudas = 70000;

        int edadCliente = 30;
        double saldoCuenta = 20000;
        boolean saldoConsistente = true;
        double totalDepositos = 30000;
        int antiguedadCuenta = 3;
        double porcentajeRetiroReciente = 20;

        double cuotaMensual = 8575;
        given(prestamoService.calcularCuotaMensual(solicitud.getMontoDelPrestamo(), solicitud.getPlazo(), solicitud.getTasaDeInteresAnual()))
                .willReturn(cuotaMensual);

        // Act
        boolean resultado = evaluacionService.evaluarSolicitud(solicitud, ingresosMensuales, buenHistorialCrediticio,
                antiguedadLaboral, totalDeudas, edadCliente,
                saldoCuenta, saldoConsistente, totalDepositos, antiguedadCuenta, porcentajeRetiroReciente);

        // Assert
        assertFalse(resultado);
        assertEquals("Rechazada", solicitud.getEstado());
        assertEquals(List.of("La relación deuda/ingreso supera el 50%"), solicitud.getRazonesRechazo());
        verify(solicitudPrestamoRepository, times(1)).save(solicitud);
    }

    @Test
    public void evaluarSolicitud_WhenClientAgeExceedsLimit_ShouldReturnFalseAndSetRejected() {
        // Arrange
        SolicitudEntity solicitud = new SolicitudEntity();
        solicitud.setMontoDelPrestamo(100000);
        solicitud.setPlazo(40);
        solicitud.setTasaDeInteresAnual(5.0);

        double ingresosMensuales = 300000;
        boolean buenHistorialCrediticio = true;
        int antiguedadLaboral = 5;
        double totalDeudas = 50000;
        int edadCliente = 40;
        double saldoCuenta = 50000;
        boolean saldoConsistente = true;
        double totalDepositos = 40000;
        int antiguedadCuenta = 5;
        double porcentajeRetiroReciente = 10;

        // Simular el cálculo de cuota mensual
        double cuotaMensual = 2684.11;
        given(prestamoService.calcularCuotaMensual(solicitud.getMontoDelPrestamo(), solicitud.getPlazo(), solicitud.getTasaDeInteresAnual()))
                .willReturn(cuotaMensual);


        boolean resultado = evaluacionService.evaluarSolicitud(solicitud, ingresosMensuales, buenHistorialCrediticio,
                antiguedadLaboral, totalDeudas, edadCliente,
                saldoCuenta, saldoConsistente, totalDepositos, antiguedadCuenta, porcentajeRetiroReciente);


        assertFalse(resultado);
        assertEquals("Rechazada", solicitud.getEstado());
        assertEquals(List.of("La edad del cliente al finalizar el préstamo superará los 75 años"), solicitud.getRazonesRechazo());
        verify(solicitudPrestamoRepository, times(1)).save(solicitud);
    }

    @Test
    public void evaluarSolicitud_WhenSavingsCapacityIsInsufficient_ShouldReturnFalseAndSetRejected() {

        SolicitudEntity solicitud = new SolicitudEntity();
        solicitud.setMontoDelPrestamo(100000);
        solicitud.setPlazo(10);
        solicitud.setTasaDeInteresAnual(5.0);

        double ingresosMensuales = 300000;
        boolean buenHistorialCrediticio = true;
        int antiguedadLaboral = 5;
        double totalDeudas = 50000;
        int edadCliente = 35;
        double saldoCuenta = 5000;
        boolean saldoConsistente = false;
        double totalDepositos = 10000;
        int antiguedadCuenta = 1;
        double porcentajeRetiroReciente = 40;


        double cuotaMensual = 1200;
        given(prestamoService.calcularCuotaMensual(solicitud.getMontoDelPrestamo(), solicitud.getPlazo(), solicitud.getTasaDeInteresAnual()))
                .willReturn(cuotaMensual);


        boolean resultado = evaluacionService.evaluarSolicitud(solicitud, ingresosMensuales, buenHistorialCrediticio,
                antiguedadLaboral, totalDeudas, edadCliente,
                saldoCuenta, saldoConsistente, totalDepositos, antiguedadCuenta, porcentajeRetiroReciente);


        assertFalse(resultado);
        assertEquals("Rechazada", solicitud.getEstado());
        assertTrue(solicitud.getRazonesRechazo().contains("Capacidad de ahorro insuficiente"));
        verify(solicitudPrestamoRepository, times(1)).save(solicitud);
    }



    @Test
    public void evaluarSolicitud_WhenAgeTooHighAtEndOfLoan_ShouldReturnFalseAndSetRejected() {

        SolicitudEntity solicitud = new SolicitudEntity();
        solicitud.setMontoDelPrestamo(100000);
        solicitud.setPlazo(12);
        solicitud.setTasaDeInteresAnual(5.0);

        double ingresosMensuales = 300000;
        boolean buenHistorialCrediticio = true;
        int antiguedadLaboral = 5;
        double totalDeudas = 50000;
        int edadCliente = 70;
        double saldoCuenta = 20000;
        boolean saldoConsistente = true;
        double totalDepositos = 40000;
        int antiguedadCuenta = 3;
        double porcentajeRetiroReciente = 20;

        double cuotaMensual = 8575;
        given(prestamoService.calcularCuotaMensual(solicitud.getMontoDelPrestamo(), solicitud.getPlazo(), solicitud.getTasaDeInteresAnual()))
                .willReturn(cuotaMensual);

        boolean resultado = evaluacionService.evaluarSolicitud(solicitud, ingresosMensuales, buenHistorialCrediticio,
                antiguedadLaboral, totalDeudas, edadCliente,
                saldoCuenta, saldoConsistente, totalDepositos, antiguedadCuenta, porcentajeRetiroReciente);


        assertFalse(resultado);
        assertEquals("Rechazada", solicitud.getEstado());
        assertEquals(List.of("La edad del cliente al finalizar el préstamo superará los 75 años"), solicitud.getRazonesRechazo());
        verify(solicitudPrestamoRepository, times(1)).save(solicitud);
    }

}
