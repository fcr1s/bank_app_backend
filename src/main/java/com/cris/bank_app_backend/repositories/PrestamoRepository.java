package com.cris.bank_app_backend.repositories;

import com.cris.bank_app_backend.entities.PrestamoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PrestamoRepository extends JpaRepository<PrestamoEntity, Long> {

    PrestamoEntity findBySolicitudId(Long solicitudId);
}
