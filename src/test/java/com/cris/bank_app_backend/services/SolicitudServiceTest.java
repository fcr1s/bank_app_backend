package com.cris.bank_app_backend.services;

import com.cris.bank_app_backend.entities.ClienteEntity;
import com.cris.bank_app_backend.entities.SolicitudEntity;
import com.cris.bank_app_backend.entities.DocumentoEntity;
import com.cris.bank_app_backend.repositories.SolicitudRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class SolicitudServiceTest {

    @Mock
    private SolicitudRepository solicitudRepository;

    @Mock
    private DocumentoService documentoService;

    @Mock
    private ClienteService clienteService;

    @Mock
    private PrestamoService prestamoService;

    @InjectMocks
    private SolicitudService solicitudService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testObtenerSolicitudes() {
        SolicitudEntity solicitud1 = new SolicitudEntity();
        SolicitudEntity solicitud2 = new SolicitudEntity();
        when(solicitudRepository.findAll()).thenReturn(Arrays.asList(solicitud1, solicitud2));

        List<SolicitudEntity> solicitudes = solicitudService.obtenerSolicitudes();

        assertEquals(2, solicitudes.size());
        verify(solicitudRepository, times(1)).findAll();
    }

    @Test
    public void testObtenerSolicitudPorId_Encontrada() {
        SolicitudEntity solicitud = new SolicitudEntity();
        solicitud.setId(1L);
        when(solicitudRepository.findById(1L)).thenReturn(Optional.of(solicitud));

        SolicitudEntity result = solicitudService.obtenerSolicitudPorId(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(solicitudRepository, times(1)).findById(1L);
    }

    @Test
    public void testObtenerSolicitudPorId_NoEncontrada() {
        when(solicitudRepository.findById(1L)).thenReturn(Optional.empty());

        SolicitudEntity result = solicitudService.obtenerSolicitudPorId(1L);

        assertNull(result);
        verify(solicitudRepository, times(1)).findById(1L);
    }

    @Test
    public void testEliminarSolicitud() {
        doNothing().when(solicitudRepository).deleteById(1L);

        solicitudService.eliminarSolicitud(1L);

        verify(solicitudRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testCrearSolicitud() throws IOException {
        ClienteEntity cliente = new ClienteEntity();
        cliente.setId(1L);
        cliente.setRut("12345678-9");
        when(clienteService.obtenerClienteLogueado()).thenReturn(cliente);

        MultipartFile file = mock(MultipartFile.class);
        when(file.getBytes()).thenReturn(new byte[]{1, 2, 3});

        solicitudService.crearSolicitud("primera vivienda", 500000, 200000, 4.5, 15, List.of(file));

        verify(prestamoService, times(1)).verificarRestricciones("primera vivienda", 500000, 200000, 15, 4.5);
        verify(solicitudRepository, times(1)).save(any(SolicitudEntity.class));
        verify(documentoService, times(1)).saveDocument(any(DocumentoEntity.class));
    }

    @Test
    public void testCrearSolicitud_ErrorAlGuardarDocumento() throws IOException {
        ClienteEntity cliente = new ClienteEntity();
        cliente.setId(1L);
        cliente.setRut("12345678-9");
        when(clienteService.obtenerClienteLogueado()).thenReturn(cliente);

        MultipartFile file = mock(MultipartFile.class);
        when(file.getBytes()).thenThrow(new IOException("Error al leer archivo"));

        assertThrows(IllegalArgumentException.class, () ->
                solicitudService.crearSolicitud("primera vivienda", 500000, 200000, 4.5, 15, List.of(file))
        );

        verify(prestamoService, times(1)).verificarRestricciones("primera vivienda", 500000, 200000, 15, 4.5);
        verify(solicitudRepository, times(1)).save(any(SolicitudEntity.class));
        verify(documentoService, never()).saveDocument(any(DocumentoEntity.class));
    }

    @Test
    public void testObtenerSolicitudesDelCliente() {
        ClienteEntity cliente = new ClienteEntity();
        cliente.setId(1L);
        when(clienteService.obtenerClienteLogueado()).thenReturn(cliente);

        SolicitudEntity solicitud = new SolicitudEntity();
        solicitud.setClienteId(1L);
        when(solicitudRepository.findByClienteId(1L)).thenReturn(List.of(solicitud));

        List<SolicitudEntity> result = solicitudService.obtenerSolicitudesDelCliente();

        assertEquals(1, result.size());
        assertEquals(1L, result.getFirst().getClienteId());
        verify(solicitudRepository, times(1)).findByClienteId(1L);
    }

    @Test
    public void testCancelarSolicitud_CancelacionPermitida() {
        SolicitudEntity solicitud = new SolicitudEntity();
        solicitud.setId(1L);
        solicitud.setEstado("En revisión inicial");
        when(solicitudRepository.findById(1L)).thenReturn(Optional.of(solicitud));

        solicitudService.cancelarSolicitud(1L);

        assertEquals("Cancelada por el cliente", solicitud.getEstado());
        verify(solicitudRepository, times(1)).save(solicitud);
    }

    @Test
    public void testCancelarSolicitud_CancelacionNoPermitida() {
        SolicitudEntity solicitud = new SolicitudEntity();
        solicitud.setId(1L);
        solicitud.setEstado("Aprobada");
        when(solicitudRepository.findById(1L)).thenReturn(Optional.of(solicitud));

        assertThrows(IllegalArgumentException.class, () -> solicitudService.cancelarSolicitud(1L));

        verify(solicitudRepository, never()).save(solicitud);
    }

    @Test
    public void testActualizarEstadoSolicitud() {
        SolicitudEntity solicitud = new SolicitudEntity();
        solicitud.setId(1L);
        solicitud.setEstado("En revisión");
        when(solicitudRepository.findById(1L)).thenReturn(Optional.of(solicitud));

        solicitudService.actualizarEstadoSolicitud(1L, "Aprobada");

        assertEquals("Aprobada", solicitud.getEstado());
        verify(solicitudRepository, times(1)).save(solicitud);
    }

    @Test
    public void testActualizarEstadoSolicitud_NoEncontrada() {
        when(solicitudRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> solicitudService.actualizarEstadoSolicitud(1L, "Aprobada"));

        verify(solicitudRepository, never()).save(any(SolicitudEntity.class));
    }
}
