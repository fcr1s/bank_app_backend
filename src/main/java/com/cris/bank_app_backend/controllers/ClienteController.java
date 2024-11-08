package com.cris.bank_app_backend.controllers;

import com.cris.bank_app_backend.entities.ClienteEntity;
import com.cris.bank_app_backend.services.ClienteService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clientes")
@CrossOrigin("http://4.228.227.122:8080")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    // Endpoint para obtener todos los clientes en formato JSON
    @GetMapping("/")
    public ResponseEntity<List<ClienteEntity>> obtenerClientes() {
        return ResponseEntity.ok(clienteService.obtenerClientes());
    }

    // Endpoint para registrar un nuevo cliente
    @PostMapping("/registrarse")
    public ResponseEntity<String> registrarCliente(@RequestBody ClienteEntity cliente) {
        try {
            ClienteEntity nuevoCliente = clienteService.registrarCliente(cliente);
            return ResponseEntity.ok("Cliente registrado con éxito: " + nuevoCliente.getId());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    // Endpoint para iniciar sesión, retorna el cliente si las credenciales son correctas
    @PostMapping("/login")
    public ResponseEntity<ClienteEntity> login(@RequestParam String rut, @RequestParam String password) {
        ClienteEntity cliente = clienteService.login(rut, password);
        return cliente != null ? ResponseEntity.ok(cliente) : ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    // Endpoint para logout
    @PostMapping("/logout")
    public ResponseEntity<String> logout() {
        clienteService.logout();
        return ResponseEntity.ok("Sesión cerrada");
    }

    // Endpoint para simular un crédito
    @PostMapping("/simular-credito")
    public ResponseEntity<Double> simularCredito(
            @RequestParam String tipoPrestamo,
            @RequestParam double valorPropiedad,
            @RequestParam double montoPrestamo,
            @RequestParam int plazo,
            @RequestParam double tasaInteresAnual) {
        try {
            double cuotaMensual = clienteService.simularCredito(tipoPrestamo, valorPropiedad, montoPrestamo, plazo, tasaInteresAnual);
            return ResponseEntity.ok(cuotaMensual);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }

}
