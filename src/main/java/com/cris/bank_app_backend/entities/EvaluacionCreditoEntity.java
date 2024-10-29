package com.cris.bank_app_backend.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

@Entity
@Table(name = "evaluacion_credito")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EvaluacionCreditoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long id;

    // Evaluación de los criterios
    private boolean cuota_ingresos;
    private boolean historial_crediticio;
    private boolean antiguedad_laboral;
    private boolean deuda_ingreso;
    private boolean monto_maximo;
    private boolean edad_solicitante;

    @OneToOne(mappedBy = "evaluacion")
    private SolicitudPrestamoEntity solicitud; // Relación uno a uno con SolicitudPrestamo

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "capacidad_ahorro_id", referencedColumnName = "id")
    private CapacidadAhorroEntity capacidadAhorro; // Relación uno a uno con CapacidadAhorroEntity
}



