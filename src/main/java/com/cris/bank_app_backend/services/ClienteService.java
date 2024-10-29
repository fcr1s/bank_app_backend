package com.cris.bank_app_backend.services;

import com.cris.bank_app_backend.entities.ClienteEntity;
import com.cris.bank_app_backend.repositories.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class ClienteService {
    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private SimulacionCreditoService simulacionCreditoService;

    public ClienteEntity findByRut(String rut){
        return clienteRepository.findByRut(rut);
    }

    public ArrayList<ClienteEntity> findAll(){
        return (ArrayList<ClienteEntity>) clienteRepository.findAll();
    }

    public ClienteEntity save(ClienteEntity cliente){
        if (clienteRegistrado(cliente.getRut())) {
            throw new IllegalArgumentException("El cliente con el RUT " + cliente.getRut() + " ya está registrado.");
        }
        return clienteRepository.save(cliente);
    }

    public void deleteById(Long id){
        clienteRepository.deleteById(id);
    }

    public boolean clienteRegistrado(String rut) {
        return clienteRepository.findByRut(rut) != null;
    }

    public double simularCredito(String rut, String tipoPrestamo, double valorPropiedad, double montoPrestamo, int plazo, double tasaInteresAnual) {
        if (!clienteRegistrado(rut)) {
            throw new IllegalArgumentException("El cliente no está registrado");
        }
        return simulacionCreditoService.calcularCuotaMensual(tipoPrestamo, valorPropiedad, montoPrestamo, plazo, tasaInteresAnual);
    }

}


