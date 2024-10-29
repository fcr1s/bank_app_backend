package com.cris.bank_app_backend.repositories;

import com.cris.bank_app_backend.entities.ClienteEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

public interface ClienteRepository extends JpaRepository<ClienteEntity, Long>{
    public ClienteEntity findByRut(String rut);
}
