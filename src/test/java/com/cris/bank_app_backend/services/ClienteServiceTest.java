package com.cris.bank_app_backend.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

import com.cris.bank_app_backend.entities.ClienteEntity;
import com.cris.bank_app_backend.repositories.ClienteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.lang.reflect.Field;
import java.util.Optional;
import java.util.List;
import java.util.Arrays;

@SpringBootTest
public class ClienteServiceTest {

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private PrestamoService prestamoService;

    @InjectMocks
    private ClienteService clienteService;


    @BeforeEach
    public void setUp() throws Exception {
        // Inicializa los mocks
        MockitoAnnotations.openMocks(this);

        // Reinicia el estado de clienteLogueado a null usando reflexión
        Field clienteLogueadoField = ClienteService.class.getDeclaredField("clienteLogueado");
        clienteLogueadoField.setAccessible(true);
        clienteLogueadoField.set(null, null);
    }

    // Prueba para obtener todos los clientes
    @Test
    public void obtenerClientes_ShouldReturnListOfClientes() {
        ClienteEntity cliente1 = new ClienteEntity(1L, "12345678-9", "Cliente Uno", "password", "email1@example.com");
        ClienteEntity cliente2 = new ClienteEntity(2L, "98765432-1", "Cliente Dos", "password", "email2@example.com");
        List<ClienteEntity> clientes = Arrays.asList(cliente1, cliente2);

        given(clienteRepository.findAll()).willReturn(clientes);

        List<ClienteEntity> result = clienteService.obtenerClientes();

        assertEquals(2, result.size());
        verify(clienteRepository, times(1)).findAll();
    }

    // Prueba para registrar un nuevo cliente (caso exitoso)
    @Test
    public void registrarCliente_ShouldRegisterNewCliente() {
        ClienteEntity cliente = new ClienteEntity(null, "12345678-9", "Nuevo Cliente", "password", "email@example.com");

        given(clienteRepository.findByRut(cliente.getRut())).willReturn(Optional.empty());
        given(clienteRepository.save(cliente)).willReturn(cliente);

        ClienteEntity result = clienteService.registrarCliente(cliente);

        assertNotNull(result);
        verify(clienteRepository, times(1)).findByRut(cliente.getRut());
        verify(clienteRepository, times(1)).save(cliente);
    }

    // Prueba para registrar un nuevo cliente (caso de RUT duplicado)
    @Test
    public void registrarCliente_WithDuplicateRut_ShouldThrowException() {
        ClienteEntity cliente = new ClienteEntity(null, "12345678-9", "Cliente Duplicado", "password", "email@example.com");

        given(clienteRepository.findByRut(cliente.getRut())).willReturn(Optional.of(cliente));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> clienteService.registrarCliente(cliente));

        assertEquals("El RUT ya está registrado", exception.getMessage());
        verify(clienteRepository, times(1)).findByRut(cliente.getRut());
        verify(clienteRepository, never()).save(cliente);
    }

    // Prueba para iniciar sesión con credenciales correctas
    @Test
    public void login_WithValidCredentials_ShouldReturnCliente() {
        String rut = "12345678-9";
        String password = "password";
        ClienteEntity cliente = new ClienteEntity(1L, rut, "Cliente Uno", password, "email@example.com");

        given(clienteRepository.findByRutAndPassword(rut, password)).willReturn(Optional.of(cliente));

        ClienteEntity result = clienteService.login(rut, password);

        assertNotNull(result);
        assertEquals(cliente, result);
        verify(clienteRepository, times(1)).findByRutAndPassword(rut, password);
    }

    // Prueba para iniciar sesión con credenciales incorrectas
    @Test
    public void login_WithInvalidCredentials_ShouldReturnNull() {
        String rut = "12345678-9";
        String password = "wrongpassword";

        given(clienteRepository.findByRutAndPassword(rut, password)).willReturn(Optional.empty());

        ClienteEntity result = clienteService.login(rut, password);

        assertNull(result);
        verify(clienteRepository, times(1)).findByRutAndPassword(rut, password);
    }

    // Prueba para simular un crédito (cliente logueado)
    @Test
    public void simularCredito_WithLoggedInCliente_ShouldReturnCuotaMensual() {
        // Configurar los datos del cliente
        String rut = "12345678-9";
        String password = "password";
        ClienteEntity cliente = new ClienteEntity(1L, rut, "Cliente Uno", password, "email@example.com");

        // Registrar el cliente en el repositorio simulado
        given(clienteRepository.findByRutAndPassword(rut, password)).willReturn(Optional.of(cliente));

        // Llamar al método login para simular un cliente logueado
        clienteService.login(rut, password);

        // Definir valores para simular el crédito
        double montoPrestamo = 50000;
        int plazo = 12;
        double tasaInteresAnual = 3.5;
        double cuotaEsperada = 420.0;

        // Configurar el comportamiento de los métodos de PrestamoService
        doNothing().when(prestamoService).verificarRestricciones(anyString(), anyDouble(), anyDouble(), anyInt(), anyDouble());
        when(prestamoService.calcularCuotaMensual(montoPrestamo, plazo, tasaInteresAnual)).thenReturn(cuotaEsperada);

        // Ejecutar el método simularCredito
        double cuota = clienteService.simularCredito("Primera vivienda", 100000, montoPrestamo, plazo, tasaInteresAnual);

        // Verificar el resultado y el comportamiento de los mocks
        assertEquals(cuotaEsperada, cuota);
        verify(prestamoService, times(1)).verificarRestricciones(anyString(), anyDouble(), anyDouble(), anyInt(), anyDouble());
        verify(prestamoService, times(1)).calcularCuotaMensual(montoPrestamo, plazo, tasaInteresAnual);
    }


    // Prueba para obtener cliente logueado (caso exitoso)
    @Test
    public void obtenerClienteLogueado_WhenLoggedIn_ShouldReturnCliente() {
        // Crear y registrar el cliente
        ClienteEntity cliente = new ClienteEntity(1L, "12345678-9", "Cliente Uno", "password", "email@example.com");

        // Simular el comportamiento del repositorio
        given(clienteRepository.findByRutAndPassword(cliente.getRut(), cliente.getPassword())).willReturn(Optional.of(cliente));

        // Registrar el cliente (esto es importante para simular que el cliente existe)
        clienteService.registrarCliente(cliente);

        // Simular login
        clienteService.login(cliente.getRut(), cliente.getPassword()); // Simular cliente logueado

        // Obtener el cliente logueado
        ClienteEntity clienteLogueado = clienteService.obtenerClienteLogueado();

        // Verificaciones
        assertNotNull(clienteLogueado);
        assertEquals(cliente, clienteLogueado);
    }


    // Prueba para obtener cliente logueado sin estar logueado (debe lanzar excepción)
    @Test
    public void obtenerClienteLogueado_WithoutLoggedInCliente_ShouldThrowException() {
        // Ejecuta la prueba y verifica que se lance la excepción esperada
        Exception exception = assertThrows(IllegalStateException.class, clienteService::obtenerClienteLogueado);

        // Verifica el mensaje de la excepción
        assertEquals("No hay un cliente logueado.", exception.getMessage());
    }

}
