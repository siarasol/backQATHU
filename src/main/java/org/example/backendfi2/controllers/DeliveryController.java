package org.example.backendfi2.controllers;
import org.example.backendfi2.service.DeliveryService;
import org.example.backendfi2.dto.DeliveryRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/delivery")
public class DeliveryController {

    @Autowired
    private DeliveryService deliveryService;

    @PostMapping("/registrar")
    public ResponseEntity<Map<String, Object>> registrarDelivery(@RequestBody DeliveryRequestDTO deliveryRequest) {
        // Llama al servicio con los datos del DTO
        Map<String, Object> response = deliveryService.registrarDelivery(
                deliveryRequest.getPedidoId(),
                deliveryRequest.getDireccion(),
                deliveryRequest.getIndicaciones(),
                deliveryRequest.getLatitud(),
                deliveryRequest.getLongitud()
        );

        return ResponseEntity.ok(response);
    }
}
