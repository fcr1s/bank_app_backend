package com.cris.bank_app_backend.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "documentos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DocumentosEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long id;

    private String comprobanteIngresos;
    private String certificadoDeAvaluo;
    private String hitorialCrediticio;
    private String escrituraDePropiedad;
    private String estadoFinancieroDelNegocio;
    private String planDeNegocio;
    private String presupuestoDeLaRemodelacion;

    @OneToMany(mappedBy = "documentos")
    private List<SolicitudPrestamoEntity> solicitudes; // Relación uno a muchos con SolicitudPrestamo
}


