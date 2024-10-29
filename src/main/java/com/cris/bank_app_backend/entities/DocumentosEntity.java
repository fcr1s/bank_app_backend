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

    private String comprobante_ingresos;
    private String certificado_de_avaluo;
    private String hitorial_crediticio;
    private String escritura_de_propiedad;
    private String estado_financiero_del_negocio;
    private String plan_de_negocio;
    private String presupuesto_de_la_remodelacion;

    @OneToMany(mappedBy = "documentos")
    private List<SolicitudPrestamoEntity> solicitudes; // Relación uno a muchos con SolicitudPrestamo
}


