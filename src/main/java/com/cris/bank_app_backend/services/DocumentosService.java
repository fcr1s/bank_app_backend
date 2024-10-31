package com.cris.bank_app_backend.services;

import com.cris.bank_app_backend.entities.DocumentosEntity;
import com.cris.bank_app_backend.repositories.DocumentosRepository;
import com.cris.bank_app_backend.repositories.SolicitudPrestamoRepository;
import com.cris.bank_app_backend.entities.SolicitudPrestamoEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DocumentosService {

    @Autowired
    private DocumentosRepository documentosRepository;

    @Autowired
    private SolicitudPrestamoRepository solicitudPrestamoRepository;


}
