package com.cris.bank_app_backend.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

import com.cris.bank_app_backend.entities.EjecutivoEntity;
import com.cris.bank_app_backend.entities.SolicitudEntity;
import com.cris.bank_app_backend.repositories.EjecutivoRepository;
import com.cris.bank_app_backend.repositories.SolicitudRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

public class EjecutivoServiceTest {

    @Mock
    private EjecutivoRepository ejecutivoRepository;

    @Mock
    private SolicitudRepository solicitudRepository;

    @InjectMocks
    private EjecutivoService ejecutivoService;

    @BeforeEach
    public void setUp() throws Exception {
        // Inicializa los mocks
        MockitoAnnotations.openMocks(this);

        // Reinicia el estado de clienteLogueado a null usando reflexión
        Field ejecutivoLogueadoField = EjecutivoService.class.getDeclaredField("ejecutivoLogueado");
        ejecutivoLogueadoField.setAccessible(true);
        ejecutivoLogueadoField.set(null, null);
    }

    // Prueba para iniciar sesión con credenciales válidas
    @Test
    public void login_WithValidCredentials_ShouldReturnEjecutivo() {
        String rut = "12345678-9";
        String password = "password";
        EjecutivoEntity ejecutivo = new EjecutivoEntity(1L, rut, password);

        given(ejecutivoRepository.findByRutAndPassword(rut, password)).willReturn(Optional.of(ejecutivo));

        EjecutivoEntity result = ejecutivoService.login(rut, password);

        assertNotNull(result);
        assertEquals(ejecutivo, result);
        verify(ejecutivoRepository, times(1)).findByRutAndPassword(rut, password);
    }

    // Prueba para iniciar sesión con credenciales inválidas
    @Test
    public void login_WithInvalidCredentials_ShouldReturnNull() {
        String rut = "12345678-9";
        String password = "wrongpassword";

        given(ejecutivoRepository.findByRutAndPassword(rut, password)).willReturn(Optional.empty());

        EjecutivoEntity result = ejecutivoService.login(rut, password);

        assertNull(result);
        verify(ejecutivoRepository, times(1)).findByRutAndPassword(rut, password);
    }

    // Prueba para obtener el ejecutivo logueado (caso exitoso)
    @Test
    public void obtenerClienteLogueado_WhenLoggedIn_ShouldReturnEjecutivo() {
        EjecutivoEntity ejecutivo = new EjecutivoEntity(1L, "12345678-9", "password");
        given(ejecutivoRepository.findByRutAndPassword("12345678-9", "password")).willReturn(Optional.of(ejecutivo));

        // Simulamos el login
        ejecutivoService.login(ejecutivo.getRut(), ejecutivo.getPassword());

        EjecutivoEntity result = ejecutivoService.obtenerClienteLogueado();

        assertNotNull(result);
        assertEquals(ejecutivo, result);
    }

    // Prueba para obtener el ejecutivo logueado sin estar logueado (debe lanzar excepción)
    @Test
    public void obtenerClienteLogueado_WithoutLoggedInEjecutivo_ShouldThrowException() {
        Exception exception = assertThrows(IllegalStateException.class, ejecutivoService::obtenerClienteLogueado);
        assertEquals("No hay un ejecutivo logueado.", exception.getMessage());
    }

    // Prueba para obtener todas las solicitudes
    @Test
    public void obtenerTodasLasSolicitudes_ShouldReturnListOfSolicitudes() {
        SolicitudEntity solicitud1 = new SolicitudEntity();
        SolicitudEntity solicitud2 = new SolicitudEntity();
        List<SolicitudEntity> solicitudes = Arrays.asList(solicitud1, solicitud2);

        given(solicitudRepository.findAll()).willReturn(solicitudes);

        List<SolicitudEntity> result = ejecutivoService.obtenerTodasLasSolicitudes();

        assertEquals(2, result.size());
        verify(solicitudRepository, times(1)).findAll();
    }

    // Prueba para obtener solicitudes por estado (caso exitoso)
    @Test
    public void obtenerSolicitudesPorEstado_ValidEstado_ShouldReturnSolicitudes() {
        String estado = "En revisión inicial";
        SolicitudEntity solicitud = new SolicitudEntity();
        List<SolicitudEntity> solicitudes = List.of(solicitud);

        given(solicitudRepository.findByEstado(estado)).willReturn(solicitudes);

        List<SolicitudEntity> result = ejecutivoService.obtenerSolicitudesPorEstado(estado);

        assertEquals(1, result.size());
        verify(solicitudRepository, times(1)).findByEstado(estado);
    }

    // Prueba para obtener solicitudes por estado (estado no válido)
    @Test
    public void obtenerSolicitudesPorEstado_InvalidEstado_ShouldThrowException() {
        String estado = "Estado no válido";

        Exception exception = assertThrows(IllegalArgumentException.class, () -> ejecutivoService.obtenerSolicitudesPorEstado(estado));

        assertEquals("Estado no válido", exception.getMessage());
    }

    // Prueba para actualizar el estado de una solicitud (caso exitoso)
    @Test
    public void actualizarEstadoSolicitud_ValidIdAndEstado_ShouldUpdateSolicitud() {
        Long id = 1L;
        String nuevoEstado = "Aprobada";
        SolicitudEntity solicitud = new SolicitudEntity();
        solicitud.setId(id);
        solicitud.setEstado("En revisión inicial");

        given(solicitudRepository.findById(id)).willReturn(Optional.of(solicitud));

        ejecutivoService.actualizarEstadoSolicitud(id, nuevoEstado);

        assertEquals(nuevoEstado, solicitud.getEstado());
        verify(solicitudRepository, times(1)).save(solicitud);
    }

    // Prueba para actualizar el estado de una solicitud (estado no válido)
    @Test
    public void actualizarEstadoSolicitud_InvalidEstado_ShouldThrowException() {
        Long id = 1L;
        String nuevoEstado = "Estado no válido";
        SolicitudEntity solicitud = new SolicitudEntity();
        solicitud.setId(id);
        solicitud.setEstado("En revisión inicial");

        given(solicitudRepository.findById(id)).willReturn(Optional.of(solicitud));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> ejecutivoService.actualizarEstadoSolicitud(id, nuevoEstado));

        assertEquals("Estado no válido", exception.getMessage());
        verify(solicitudRepository, never()).save(solicitud);
    }

    // Prueba para actualizar el estado de una solicitud (solicitud no encontrada)
    @Test
    public void actualizarEstadoSolicitud_SolicitudNoEncontrada_ShouldThrowException() {
        Long id = 1L;
        String nuevoEstado = "Aprobada";

        given(solicitudRepository.findById(id)).willReturn(Optional.empty());

        Exception exception = assertThrows(NoSuchElementException.class, () -> ejecutivoService.actualizarEstadoSolicitud(id, nuevoEstado));

        assertEquals("Solicitud no encontrada", exception.getMessage());
    }

    // Prueba para registrar un nuevo ejecutivo (caso exitoso)
    @Test
    public void registrarEjecutivo_ValidEjecutivo_ShouldReturnEjecutivo() {
        EjecutivoEntity ejecutivo = new EjecutivoEntity();
        ejecutivo.setRut("12345678-9");
        ejecutivo.setPassword("password");

        given(ejecutivoRepository.findByRut(ejecutivo.getRut())).willReturn(Optional.empty());
        given(ejecutivoRepository.save(ejecutivo)).willReturn(ejecutivo);

        EjecutivoEntity result = ejecutivoService.registrarEjecutivo(ejecutivo);

        assertNotNull(result);
        assertEquals(ejecutivo, result);
        verify(ejecutivoRepository, times(1)).findByRut(ejecutivo.getRut());
        verify(ejecutivoRepository, times(1)).save(ejecutivo);
    }

    // Prueba para registrar un ejecutivo que ya existe (debe lanzar excepción)
    @Test
    public void registrarEjecutivo_EjecutivoYaRegistrado_ShouldThrowException() {
        EjecutivoEntity ejecutivo = new EjecutivoEntity();
        ejecutivo.setRut("12345678-9");
        ejecutivo.setPassword("password");

        given(ejecutivoRepository.findByRut(ejecutivo.getRut())).willReturn(Optional.of(ejecutivo));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> ejecutivoService.registrarEjecutivo(ejecutivo));

        assertEquals("El RUT ya está registrado", exception.getMessage());
        verify(ejecutivoRepository, times(1)).findByRut(ejecutivo.getRut());
        verify(ejecutivoRepository, never()).save(ejecutivo); // No debe llamarse save si ya existe
    }

}
