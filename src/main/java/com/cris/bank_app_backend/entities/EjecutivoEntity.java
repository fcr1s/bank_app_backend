package com.cris.bank_app_backend.entities;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "ejecutivo")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EjecutivoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long id;
    private String name;
    private String email;
    private String phone;
    private String rut;
    private String password;

    @OneToMany(mappedBy = "ejecutivo")
    private List<SolicitudPrestamoEntity> solicitudes; // Relación uno a muchos con SolicitudPrestamo
}
