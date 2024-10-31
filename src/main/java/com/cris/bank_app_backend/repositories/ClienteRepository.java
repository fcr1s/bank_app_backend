package com.cris.bank_app_backend.repositories;

import com.cris.bank_app_backend.entities.ClienteEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<ClienteEntity, Long>{
    public ClienteEntity findByRut(String rut);
}
