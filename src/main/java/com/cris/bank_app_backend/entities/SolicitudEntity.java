package com.cris.bank_app_backend.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "solicitud_prestamo")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SolicitudEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long id;
    private String rut;
    private Date fechaDeSolicitud;
    private String estado;
    private String tipoPrestamo;
    private int plazo;
    private double montoDelPrestamo;
    private double tasaDeInteresAnual;
    private Long clienteId; // Almacena el ID del cliente
    private Long prestamoId; // Almacena el ID del préstamo
}




