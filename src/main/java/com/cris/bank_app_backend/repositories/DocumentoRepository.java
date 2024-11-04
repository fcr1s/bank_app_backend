package com.cris.bank_app_backend.repositories;

import com.cris.bank_app_backend.entities.DocumentoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocumentoRepository extends JpaRepository<DocumentoEntity, Long> {
    List<DocumentoEntity> findBySolicitudId(Long solicitudId);
}

