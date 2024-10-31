package com.cris.bank_app_backend.repositories;

import com.cris.bank_app_backend.entities.DocumentosEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentosRepository extends JpaRepository<DocumentosEntity, Long> {

}
