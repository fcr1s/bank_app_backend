package com.cris.bank_app_backend.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;


@Entity
@Table(name = "prestamos")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class PrestamoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long id;
    private String tipoPrestamo;
    private int plazo;
    private int numeroDeCuotas;
    private int montoDelPrestamo;
    private double tasaDeInteresAnual;
    private double tasaDeInteresMensual;
    private int cuotaMensual;
    private int seguroDeDesgravamen;
    private int seguroDeIncendio;
    private int comisionPorAdministracion;
    private int costoMensual;
    private int costosTotales;
    private Long solicitudId;
}
