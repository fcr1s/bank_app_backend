package com.cris.bank_app_backend.repositories;

import com.cris.bank_app_backend.entities.EjecutivoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EjecutivoRepository extends JpaRepository<EjecutivoEntity, Long> {
    public EjecutivoEntity findByRut(String rut);
}
