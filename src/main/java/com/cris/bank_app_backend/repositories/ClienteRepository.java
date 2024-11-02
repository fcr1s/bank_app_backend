package com.cris.bank_app_backend.repositories;

import com.cris.bank_app_backend.entities.ClienteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<ClienteEntity, Long> {
    Optional<ClienteEntity> findByRutAndPassword(String rut, String password);
    Optional<ClienteEntity> findByRut(String rut);
}
