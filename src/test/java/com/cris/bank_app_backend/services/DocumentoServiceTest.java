package com.cris.bank_app_backend.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

import com.cris.bank_app_backend.entities.DocumentoEntity;
import com.cris.bank_app_backend.repositories.DocumentoRepository;
import com.cris.bank_app_backend.services.DocumentoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class DocumentoServiceTest {

    @Mock
    private DocumentoRepository documentoRepository;

    @InjectMocks
    private DocumentoService documentoService;

    @BeforeEach
    public void setUp() {
        // Inicializa los mocks
        MockitoAnnotations.openMocks(this);
    }

    // Prueba para obtener un documento por ID (documento existe)
    @Test
    public void obtenerDocumentoPorId_Existente_ShouldReturnDocumento() {
        Long id = 1L;
        DocumentoEntity documento = new DocumentoEntity();
        documento.setId(id);
        documento.setSolicitudId(1L);
        documento.setDocumento("Contenido del documento".getBytes());

        given(documentoRepository.findById(id)).willReturn(Optional.of(documento));

        DocumentoEntity result = documentoService.obtenerDocumentoPorId(id);

        assertNotNull(result);
        assertEquals(documento, result);
        verify(documentoRepository, times(1)).findById(id);
    }

    // Prueba para obtener un documento por ID (documento no existe)
    @Test
    public void obtenerDocumentoPorId_NoExistente_ShouldReturnNull() {
        Long id = 1L;

        given(documentoRepository.findById(id)).willReturn(Optional.empty());

        DocumentoEntity result = documentoService.obtenerDocumentoPorId(id);

        assertNull(result);
        verify(documentoRepository, times(1)).findById(id);
    }

    // Prueba para obtener documentos por solicitudId
    @Test
    public void obtenerDocumentosPorSolicitudId_ShouldReturnListOfDocumentos() {
        Long solicitudId = 1L;
        DocumentoEntity documento1 = new DocumentoEntity();
        DocumentoEntity documento2 = new DocumentoEntity();
        documento1.setSolicitudId(solicitudId);
        documento2.setSolicitudId(solicitudId);
        List<DocumentoEntity> documentos = Arrays.asList(documento1, documento2);

        given(documentoRepository.findBySolicitudId(solicitudId)).willReturn(documentos);

        List<DocumentoEntity> result = documentoService.obtenerDocumentosPorSolicitudId(solicitudId);

        assertEquals(2, result.size());
        verify(documentoRepository, times(1)).findBySolicitudId(solicitudId);
    }

    // Prueba para guardar un documento
    @Test
    public void saveDocument_ShouldInvokeRepositorySave() {
        DocumentoEntity documento = new DocumentoEntity();

        documentoService.saveDocument(documento);

        verify(documentoRepository, times(1)).save(documento);
    }

    // Prueba para guardar un documento desde un archivo (caso exitoso)
    @Test
    public void guardarDocumento_ShouldSaveDocumento() throws IOException {
        MultipartFile archivo = mock(MultipartFile.class);
        DocumentoEntity documento = new DocumentoEntity();
        byte[] contenidoArchivo = "Contenido del documento".getBytes();

        when(archivo.getBytes()).thenReturn(contenidoArchivo);

        documentoService.guardarDocumento(archivo, documento);

        assertArrayEquals(contenidoArchivo, documento.getDocumento());
        verify(documentoRepository, times(1)).save(documento);
    }

    // Prueba para manejar excepciones al guardar un documento desde un archivo
    @Test
    public void guardarDocumento_WhenIOException_ShouldThrowRuntimeException() throws IOException {
        MultipartFile archivo = mock(MultipartFile.class);
        DocumentoEntity documento = new DocumentoEntity();

        // Configurar el mock para lanzar IOException
        when(archivo.getBytes()).thenThrow(new IOException("Error al leer el archivo"));

        Exception exception = assertThrows(RuntimeException.class, () -> {
            documentoService.guardarDocumento(archivo, documento);
        });

        assertEquals("Error al guardar el documento: Error al leer el archivo", exception.getMessage());
        verify(documentoRepository, never()).save(documento);
    }
}
