package com.cris.bank_app_backend.services;

import com.cris.bank_app_backend.entities.ClienteEntity;
import com.cris.bank_app_backend.repositories.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private PrestamoService prestamoService;

    // Método para obtener todos los clientes
    public List<ClienteEntity> obtenerClientes() {
        return clienteRepository.findAll();
    }

    // Variable para almacenar el cliente logueado
    private static ClienteEntity clienteLogueado;

    // Método para registrar un nuevo cliente
    public ClienteEntity registrarCliente(ClienteEntity cliente) {
        // Verificar si ya existe un cliente con el mismo RUT
        if (clienteRepository.findByRut(cliente.getRut()).isPresent()) {
            throw new IllegalArgumentException("El RUT ya está registrado");
        }
        return clienteRepository.save(cliente);
    }

    // Método para iniciar sesión
    public ClienteEntity login(String rut, String password) {
        clienteLogueado = clienteRepository.findByRutAndPassword(rut, password).orElse(null);
        return clienteLogueado;
    }

    // Método para simular crédito (asegurándose de que el cliente esté logueado)
    public double simularCredito(String tipoPrestamo, double valorPropiedad, double montoPrestamo, int plazo, double tasaInteresAnual) {
        if (clienteLogueado == null) {
            throw new IllegalStateException("El cliente no está logueado.");
        }
        // Verificar restricciones del préstamo
        prestamoService.verificarRestricciones(tipoPrestamo, valorPropiedad, montoPrestamo, plazo, tasaInteresAnual);
        // Calcular cuota mensual del préstamo
        return prestamoService.calcularCuotaMensual(montoPrestamo, plazo, tasaInteresAnual);
    }

    // Método para cerrar sesión
    public void logout() {
        clienteLogueado = null; // Limpiar la sesión
    }

    // Método para obtener el cliente logueado
    public ClienteEntity obtenerClienteLogueado() {
        if (clienteLogueado == null) {
            throw new IllegalStateException("No hay un cliente logueado.");
        }
        return clienteLogueado;
    }
}

