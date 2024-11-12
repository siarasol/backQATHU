package org.example.backendfi2.service;

import org.example.backendfi2.model.Delivery;
import org.example.backendfi2.model.Pedido;
import org.example.backendfi2.repository.DeliveryRepository;
import org.example.backendfi2.repository.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

@Service
public class DeliveryService {

    @Autowired
    private DeliveryRepository deliveryRepository;

    @Autowired
    private PedidoRepository pedidoRepository;


    @Transactional
    public Map<String, Object> registrarDelivery(Long pedidoId, String direccion, String indicaciones, BigDecimal latitud, BigDecimal longitud) {
        try {
            // Verificar que el pedido existe
            Optional<Pedido> pedidoOptional = pedidoRepository.findById(pedidoId);
            if (!pedidoOptional.isPresent()) {
                return Map.of("success", false, "message", "Pedido no encontrado");
            }

            Pedido pedido = pedidoOptional.get();

            // Crear el registro de delivery
            Delivery delivery = new Delivery();
            delivery.setEstado("PENDIENTE");
            delivery.setDireccion(direccion);
            delivery.setIndicaciones(indicaciones);
            delivery.setLatitud(latitud);
            delivery.setLongitud(longitud);
            delivery.setCreatedAt(LocalDateTime.now());

            // Guardar el delivery primero para obtener su ID
            Delivery deliveryGuardado = deliveryRepository.save(delivery);

            // Asignar el delivery al pedido y guardar el pedido
            pedido.setDelivery(deliveryGuardado);
            pedidoRepository.save(pedido);

            return Map.of("success", true, "message", "Delivery registrado exitosamente y asignado al pedido.");
        } catch (Exception e) {
            System.err.println("Error al registrar el delivery: " + e.getMessage());
            return Map.of("success", false, "message", "Error al registrar el delivery: " + e.getMessage());
        }
    }
}
