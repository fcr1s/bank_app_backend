package com.cris.bank_app_backend.controllers;

import com.cris.bank_app_backend.entities.ClienteEntity;
import com.cris.bank_app_backend.services.ClienteService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
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
    public ResponseEntity<List<ClienteEntity>> findAll(){
        return ResponseEntity.ok(clienteService.findAll());
    }

    @GetMapping("/clientes/{rut}")
    public ResponseEntity<ClienteEntity> findByRut(@PathVariable String rut){
        return ResponseEntity.ok(clienteService.findByRut(rut));
    }

    @PostMapping("/clientes")
    public ResponseEntity<String> save(@RequestBody ClienteEntity cliente){
        clienteService.save(cliente);
        return ResponseEntity.ok("Cliente registrado exitosamente");
    }
    @DeleteMapping("/clientes/{id}")
    public ResponseEntity<?> deleteById(@PathVariable Long id){
        clienteService.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/simulacion-credito")
    public ResponseEntity<Double> simularCredito(
            @RequestParam String rut,
            @RequestParam String tipoPrestamo,
            @RequestParam double valorPropiedad,
            @RequestParam double montoPrestamo,
            @RequestParam int plazo,
            @RequestParam double tasaInteresAnual) {

        double cuotaMensual = clienteService.simularCredito(rut, tipoPrestamo, valorPropiedad, montoPrestamo, plazo, tasaInteresAnual);
        return ResponseEntity.ok(cuotaMensual);
    }

}
