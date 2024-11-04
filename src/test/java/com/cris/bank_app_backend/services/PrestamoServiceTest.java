package com.cris.bank_app_backend.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.cris.bank_app_backend.entities.PrestamoEntity;
import com.cris.bank_app_backend.entities.SolicitudEntity;
import com.cris.bank_app_backend.repositories.PrestamoRepository;
import com.cris.bank_app_backend.services.PrestamoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.Arrays;
import java.util.List;

public class PrestamoServiceTest {

    @Mock
    private PrestamoRepository prestamoRepository;

    @InjectMocks
    private PrestamoService prestamoService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void guardarPrestamo_ShouldSavePrestamo() {
        // Arrange
        PrestamoEntity prestamo = new PrestamoEntity();
        prestamo.setId(1L);
        when(prestamoRepository.save(prestamo)).thenReturn(prestamo);

        // Act
        PrestamoEntity result = prestamoService.guardarPrestamo(prestamo);

        // Assert
        assertEquals(prestamo, result);
        verify(prestamoRepository, times(1)).save(prestamo);
    }

    @Test
    public void obtenerTodosLosPrestamos_ShouldReturnAllPrestamos() {
        // Arrange
        PrestamoEntity prestamo1 = new PrestamoEntity();
        PrestamoEntity prestamo2 = new PrestamoEntity();
        List<PrestamoEntity> prestamos = Arrays.asList(prestamo1, prestamo2);
        when(prestamoRepository.findAll()).thenReturn(prestamos);

        // Act
        List<PrestamoEntity> result = prestamoService.obtenerTodosLosPrestamos();

        // Assert
        assertEquals(prestamos, result);
        verify(prestamoRepository, times(1)).findAll();
    }

    @Test
    public void obtenerPrestamoPorId_WhenIdExists_ShouldReturnPrestamo() {
        // Arrange
        PrestamoEntity prestamo = new PrestamoEntity();
        prestamo.setId(1L);
        when(prestamoRepository.findById(1L)).thenReturn(Optional.of(prestamo));

        // Act
        PrestamoEntity result = prestamoService.obtenerPrestamoPorId(1L);

        // Assert
        assertEquals(prestamo, result);
        verify(prestamoRepository, times(1)).findById(1L);
    }

    @Test
    public void obtenerPrestamoPorId_WhenIdDoesNotExist_ShouldReturnNull() {
        // Arrange
        when(prestamoRepository.findById(1L)).thenReturn(Optional.empty());

        // Act
        PrestamoEntity result = prestamoService.obtenerPrestamoPorId(1L);

        // Assert
        assertNull(result);
        verify(prestamoRepository, times(1)).findById(1L);
    }

    @Test
    public void obtenerPrestamoPorSolicitudId_ShouldReturnPrestamo() {
        // Arrange
        PrestamoEntity prestamo = new PrestamoEntity();
        prestamo.setSolicitudId(1L);
        when(prestamoRepository.findBySolicitudId(1L)).thenReturn(prestamo);

        // Act
        PrestamoEntity result = prestamoService.obtenerPrestamoPorSolicitudId(1L);

        // Assert
        assertEquals(prestamo, result);
        verify(prestamoRepository, times(1)).findBySolicitudId(1L);
    }

    @Test
    public void eliminarPrestamo_ShouldDeletePrestamo() {
        // Act
        prestamoService.eliminarPrestamo(1L);

        // Assert
        verify(prestamoRepository, times(1)).deleteById(1L);
    }

    @Test
    public void calcularCuotaMensual_ShouldCalculateCorrectMonthlyPayment() {
        // Arrange
        double montoPrestamo = 100000;
        int plazo = 30;
        double tasaInteresAnual = 5.0;

        // Act
        double cuotaMensual = prestamoService.calcularCuotaMensual(montoPrestamo, plazo, tasaInteresAnual);

        // Assert
        assertEquals(536.82, cuotaMensual, 0.01); // Resultado esperado
    }

    @Test
    public void verificarRestricciones_WhenInvalidForPrimeraVivienda_ShouldThrowException() {
        // Arrange
        double valorPropiedad = 100000;
        double montoPrestamo = 90000; // Más del 80% del valor de la propiedad
        int plazo = 35; // Excede los 30 años
        double tasaInteresAnual = 3.0; // Debajo del mínimo de 3.5%

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                prestamoService.verificarRestricciones("primera vivienda", valorPropiedad, montoPrestamo, plazo, tasaInteresAnual)
        );
        assertEquals("Parámetros inválidos para primera vivienda", exception.getMessage());
    }

    @Test
    public void verificarRestricciones_WhenValidForPrimeraVivienda_ShouldNotThrowException() {
        // Arrange
        double valorPropiedad = 100000;
        double montoPrestamo = 80000;
        int plazo = 30;
        double tasaInteresAnual = 4.0;

        // Act & Assert
        assertDoesNotThrow(() ->
                prestamoService.verificarRestricciones("primera vivienda", valorPropiedad, montoPrestamo, plazo, tasaInteresAnual)
        );
    }

    @Test
    public void calcularPrestamo_ShouldCalculateAndSaveLoan() {
        // Arrange
        SolicitudEntity solicitud = new SolicitudEntity();
        solicitud.setId(1L);
        solicitud.setMontoDelPrestamo(100000);
        solicitud.setPlazo(30);
        solicitud.setTasaDeInteresAnual(5.0);
        solicitud.setTipoPrestamo("primera vivienda");

        // Act
        prestamoService.calcularPrestamo(solicitud);

        // Assert
        verify(prestamoRepository, times(1)).save(any(PrestamoEntity.class));
    }
}
