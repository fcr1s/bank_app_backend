package com.cris.bank_app_backend.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

@Entity
@Table(name = "capacidad_ahorro")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CapacidadAhorroEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long id;

    private boolean saldo_minimo;
    private boolean historial_de_ahorro;
    private boolean depositos_periodicos;
    private boolean saldo_antiguedad;
    private boolean retiros_recientes;

    @OneToOne(mappedBy = "capacidadAhorro")
    private EvaluacionCreditoEntity evaluacion; // Relación uno a uno con EvaluacionCreditoEntity
}

