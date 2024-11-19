package org.example.backendfi2.controllers;
import org.example.backendfi2.service.EmailService;
import org.example.backendfi2.service.PagoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequestMapping("/api/pagos")
public class PagoController {

    @Autowired
    private PagoService pagoService;


    @Autowired
    private EmailService emailService;

    @PostMapping("/registrar")
    public ResponseEntity<Map<String, Object>> registrarPago(
            @RequestParam Long pedidoId,
            @RequestParam MultipartFile archivo,
            @RequestParam BigDecimal total,
            @RequestParam String tipo) {

        Map<String, Object> response;
        try {
            // Registrar el pago
            response = pagoService.registrarPago(pedidoId, archivo, total, tipo);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response = Map.of("success", false, "message", "Error al registrar el pago: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

}
