package com.cris.bank_app_backend.entities;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "cliente")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClienteEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long id;

    @NotBlank
    private String name;

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String phone;

    @NotBlank
    private String address;

    @NotBlank
    private Date birthdate;

    @NotBlank
    @Column(unique = true)  // Asegura que el RUT sea único
    private String rut;

    @NotBlank
    private String password;

    @OneToMany
    private List<SolicitudPrestamoEntity> solicitudes; // Relación uno a muchos con SolicitudPrestamo
}


