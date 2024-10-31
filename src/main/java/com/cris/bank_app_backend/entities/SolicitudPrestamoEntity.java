package com.cris.bank_app_backend.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

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
    private Date fechaDeSolicitud;

    @Column(nullable = false)
    private String estado = "Revisión inicial";


    @ElementCollection
    @CollectionTable(name = "documentos_pendientes", joinColumns = @JoinColumn(name = "solicitud_id"))
    @Column(name = "documento")
    private List<String> documentosPendientes; // Lista para almacenar documentos pendientes

    @ManyToOne
    @JoinColumn(name = "ejecutivo_id", nullable = false)
    private EjecutivoEntity ejecutivo;

    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    private ClienteEntity cliente;

    @ManyToOne
    @JoinColumn(name = "documentos_id", nullable = false)
    private DocumentosEntity documentos;

}




