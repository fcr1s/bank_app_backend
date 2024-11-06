package com.cris.bank_app_backend.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.cris.bank_app_backend.entities.PrestamoEntity;
import com.cris.bank_app_backend.entities.SolicitudEntity;
import com.cris.bank_app_backend.repositories.PrestamoRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;
import java.util.Arrays;
import java.util.List;


@SpringBootTest
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

        PrestamoEntity prestamo1 = new PrestamoEntity();
        PrestamoEntity prestamo2 = new PrestamoEntity();
        List<PrestamoEntity> prestamos = Arrays.asList(prestamo1, prestamo2);
        when(prestamoRepository.findAll()).thenReturn(prestamos);


        List<PrestamoEntity> result = prestamoService.obtenerTodosLosPrestamos();


        assertEquals(prestamos, result);
        verify(prestamoRepository, times(1)).findAll();
    }

    @Test
    public void obtenerPrestamoPorId_WhenIdExists_ShouldReturnPrestamo() {// Arrange
        PrestamoEntity prestamo = new PrestamoEntity();
        prestamo.setId(1L);
        when(prestamoRepository.findById(1L)).thenReturn(Optional.of(prestamo));


        PrestamoEntity result = prestamoService.obtenerPrestamoPorId(1L);


        assertEquals(prestamo, result);
        verify(prestamoRepository, times(1)).findById(1L);
    }

    @Test
    public void obtenerPrestamoPorId_WhenIdDoesNotExist_ShouldReturnNull() {

        when(prestamoRepository.findById(1L)).thenReturn(Optional.empty());


        PrestamoEntity result = prestamoService.obtenerPrestamoPorId(1L);


        assertNull(result);
        verify(prestamoRepository, times(1)).findById(1L);
    }

    @Test
    public void obtenerPrestamoPorSolicitudId_ShouldReturnPrestamo() {

        PrestamoEntity prestamo = new PrestamoEntity();
        prestamo.setSolicitudId(1L);
        when(prestamoRepository.findBySolicitudId(1L)).thenReturn(prestamo);


        PrestamoEntity result = prestamoService.obtenerPrestamoPorSolicitudId(1L);


        assertEquals(prestamo, result);
        verify(prestamoRepository, times(1)).findBySolicitudId(1L);
    }

    @Test
    public void eliminarPrestamo_ShouldDeletePrestamo() {

        prestamoService.eliminarPrestamo(1L);


        verify(prestamoRepository, times(1)).deleteById(1L);
    }

    @Test
    public void calcularCuotaMensual_ShouldCalculateCorrectMonthlyPayment() {

        double montoPrestamo = 100000;
        int plazo = 30;
        double tasaInteresAnual = 5.0;

        double cuotaMensual = prestamoService.calcularCuotaMensual(montoPrestamo, plazo, tasaInteresAnual);


        assertEquals(536.82, cuotaMensual, 0.01); // Resultado esperado
    }

    @Test
    public void verificarRestricciones_WhenInvalidForPrimeraVivienda_ShouldThrowException() {

        double valorPropiedad = 100000;
        double montoPrestamo = 90000;
        int plazo = 35;
        double tasaInteresAnual = 3.0;


        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                prestamoService.verificarRestricciones("primera vivienda", valorPropiedad, montoPrestamo, plazo, tasaInteresAnual)
        );
        assertEquals("Parámetros inválidos para primera vivienda", exception.getMessage());
    }

    @Test
    public void verificarRestricciones_WhenValidForPrimeraVivienda_ShouldNotThrowException() {

        double valorPropiedad = 100000;
        double montoPrestamo = 80000;
        int plazo = 30;
        double tasaInteresAnual = 4.0;


        assertDoesNotThrow(() ->
                prestamoService.verificarRestricciones("primera vivienda", valorPropiedad, montoPrestamo, plazo, tasaInteresAnual)
        );
    }

    @Test
    public void calcularPrestamo_ShouldCalculateAndSaveLoan() {

        SolicitudEntity solicitud = new SolicitudEntity();
        solicitud.setId(1L);
        solicitud.setMontoDelPrestamo(100000);
        solicitud.setPlazo(30);
        solicitud.setTasaDeInteresAnual(5.0);
        solicitud.setTipoPrestamo("primera vivienda");


        prestamoService.calcularPrestamo(solicitud);


        verify(prestamoRepository, times(1)).save(any(PrestamoEntity.class));
    }
}
