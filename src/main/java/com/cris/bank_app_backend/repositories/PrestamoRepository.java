package com.cris.bank_app_backend.repositories;

import com.cris.bank_app_backend.entities.PrestamoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PrestamoRepository extends JpaRepository<PrestamoEntity, Long> {

}
