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
    private SimulacionCreditoService simulacionCreditoService;

    public ClienteEntity findByRut(String rut) {
        return clienteRepository.findByRut(rut);
    }

    public List<ClienteEntity> findAll() {
        return clienteRepository.findAll();
    }

    public ClienteEntity save(ClienteEntity cliente) {
        if (clienteRepository.findByRut(cliente.getRut()) != null) {
            throw new IllegalArgumentException("El cliente ya existe");
        }
        return clienteRepository.save(cliente);
    }

    public void deleteById(Long id) {
        clienteRepository.deleteById(id);
    }

    public double simularCredito(String tipoPrestamo, double valorPropiedad, double montoPrestamo, int plazo, double tasaInteresAnual) {
        return simulacionCreditoService.calcularCuotaMensual(tipoPrestamo, valorPropiedad, montoPrestamo, plazo, tasaInteresAnual);
    }

    public String login(String rut, String password) {
        ClienteEntity cliente = clienteRepository.findByRut(rut);
        if (cliente != null && cliente.getPassword().equals(password)) {
            return UUID.randomUUID().toString(); // Genera un token único y lo retorna
        }
        throw new IllegalArgumentException("RUT o contraseña incorrectos");
    }

    public ClienteEntity updateCliente(String rut, ClienteEntity clienteActualizado) {
        ClienteEntity clienteExistente = clienteRepository.findByRut(rut);
        if (clienteExistente == null) {
            throw new IllegalArgumentException("Cliente no encontrado");
        }
        clienteExistente.setName(clienteActualizado.getName());
        clienteExistente.setEmail(clienteActualizado.getEmail());
        clienteExistente.setPhone(clienteActualizado.getPhone());
        clienteExistente.setAddress(clienteActualizado.getAddress());
        clienteExistente.setBirthdate(clienteActualizado.getBirthdate());

        return clienteRepository.save(clienteExistente);
    }
}

