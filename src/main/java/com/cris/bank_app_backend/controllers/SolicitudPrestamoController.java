package com.cris.bank_app_backend.controllers;

import com.cris.bank_app_backend.entities.SolicitudPrestamoEntity;
import com.cris.bank_app_backend.services.SolicitudPrestamoService;
import com.cris.bank_app_backend.entities.DocumentosEntity;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/solicitudes")
public class SolicitudPrestamoController {

    @Autowired
    private SolicitudPrestamoService solicitudPrestamoService;


}
