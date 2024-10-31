package com.cris.bank_app_backend.controllers;

import com.cris.bank_app_backend.entities.ClienteEntity;
import com.cris.bank_app_backend.services.ClienteService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;



@RestController
@RequestMapping("/api")
@CrossOrigin("*")
public class ClienteController {
    @Autowired
    private ClienteService clienteService;

    @GetMapping("/clientes")
    public ResponseEntity<List<ClienteEntity>> findAll() {
        return ResponseEntity.ok(clienteService.findAll());
    }

    @PostMapping("/clientes")
    public ResponseEntity<ClienteEntity> save(@RequestBody ClienteEntity cliente) {
        return ResponseEntity.ok(clienteService.save(cliente));
    }

    @PutMapping("/clientes")
    public ResponseEntity<ClienteEntity> updateCliente(@RequestBody ClienteEntity clienteActualizado, HttpSession session) {
        String rut = (String) session.getAttribute("rut");
        if (rut == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok(clienteService.updateCliente(rut, clienteActualizado));
    }

    @DeleteMapping("/clientes/{id}")
    public ResponseEntity<?> deleteById(@PathVariable Long id) {
        clienteService.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/clientes/simulacion-credito")
    public ResponseEntity<Double> simularCredito(
            @RequestParam String tipoPrestamo,
            @RequestParam double valorPropiedad,
            @RequestParam double montoPrestamo,
            @RequestParam int plazo,
            @RequestParam double tasaInteresAnual, HttpSession session) {
        String token = (String) session.getAttribute("token");
        if (token == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null); // No autorizado si no hay token en sesión
        }
        double cuotaMensual = clienteService.simularCredito(tipoPrestamo, valorPropiedad, montoPrestamo, plazo, tasaInteresAnual);
        return ResponseEntity.ok(cuotaMensual);
    }

    @PostMapping("/clientes/login")
    public ResponseEntity<String> login(
            @RequestParam String rut,
            @RequestParam String password,
            HttpSession session) {
        String token = clienteService.login(rut, password);
        if (token != null) {
            session.setAttribute("token", token); // Guarda el token en la sesión
            session.setAttribute("rut", rut);     // Guarda el RUT en la sesión
            return ResponseEntity.ok(token);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales inválidas");
    }

    @PostMapping("/clientes/logout")
    public ResponseEntity<String> logout(HttpSession session) {
        session.invalidate(); // Invalida la sesión completa (incluido el token)
        return ResponseEntity.ok("Sesión cerrada exitosamente");
    }

    @GetMapping("/clientes/informacion")
    public ResponseEntity<ClienteEntity> getClienteInfo(HttpSession session) {
        String rut = (String) session.getAttribute("rut");
        if (rut == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null); // No autorizado si no hay RUT en sesión
        }
        ClienteEntity cliente = clienteService.findByRut(rut);
        return ResponseEntity.ok(cliente);
    }
}
