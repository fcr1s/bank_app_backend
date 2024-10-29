package com.cris.bank_app_backend.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.util.Date;

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
    private String tipo_prestamo;
    private Date fecha_de_inicio;
    private String plazo;
    private int monto_del_prestamo;
    private int tasa_de_interes_anual;
    private int tasa_de_interes_mensual;
    private int cuota_mensual;
    private int seguro_de_desgravamen;
    private int seguro_de_incendio;
    private int comision_por_administracion;
    private int costo_mensual;
    private int costos_totales;
}
