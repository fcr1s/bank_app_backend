package com.cris.bank_app_backend.repositories;


import com.cris.bank_app_backend.entities.SolicitudEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SolicitudRepository extends JpaRepository<SolicitudEntity, Long> {


    List<SolicitudEntity> findByClienteId(Long clienteId);
    List<SolicitudEntity> findByEstado(String estado);
}
