package com.cris.bank_app_backend.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "solicitud_prestamo")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SolicitudPrestamoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long id;
    private String estado;
    private Date fecha_de_solicitud;
    private Date fecha_de_respuesta;

    @ManyToOne
    @JoinColumn(name = "ejecutivo_id", nullable = false)
    private EjecutivoEntity ejecutivo; // Relación muchos a uno con EjecutivoEntity

    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    private ClienteEntity cliente; // Relación muchos a uno con ClienteEntity

    @ManyToOne
    @JoinColumn(name = "documentos_id", nullable = false)
    private DocumentosEntity documentos; // Relación muchos a uno con DocumentosEntity

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "evaluacion_id", referencedColumnName = "id")
    private EvaluacionCreditoEntity evaluacion; // Relación uno a uno con EvaluacionCredito
}


