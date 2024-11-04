package com.cris.bank_app_backend.repositories;

import com.cris.bank_app_backend.entities.EjecutivoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EjecutivoRepository extends JpaRepository<EjecutivoEntity, Long> {
    Optional<EjecutivoEntity> findByRutAndPassword(String rut, String password);
    Optional<EjecutivoEntity> findByRut(String rut);
}

