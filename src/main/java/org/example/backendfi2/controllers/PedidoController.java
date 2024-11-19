package org.example.backendfi2.controllers;

import org.example.backendfi2.dto.PedidoEstadoDTO;
import org.example.backendfi2.service.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;

    // Endpoint para listar pedidos por usuario
    @GetMapping("/usuario/{usuarioId}")
    public Map<String, Object> listarPedidosPorUsuario(@PathVariable Long usuarioId) {
        Map<String, Object> response = new HashMap<>();
        try {
            Object pedidos = pedidoService.listarPedidosPorUsuario(usuarioId);
            response.put("success", true);
            response.put("message", "Pedidos encontrados");
            response.put("result", pedidos);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al listar pedidos: " + e.getMessage());
            response.put("result", null);
        }
        return response;
    }

    // Endpoint para obtener detalles de un pedido específico
    @GetMapping("/{pedidoId}/detalle")
    public Map<String, Object> obtenerDetallePedido(@PathVariable Long pedidoId) {
        Map<String, Object> response = new HashMap<>();
        try {
            Object detallePedido = pedidoService.obtenerDetallePedido(pedidoId);
            response.put("success", true);
            response.put("message", "Detalle del pedido encontrado");
            response.put("result", detallePedido);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al obtener el detalle del pedido: " + e.getMessage());
            response.put("result", null);
        }
        return response;
    }

    @GetMapping("/estados")
    public ResponseEntity<List<PedidoEstadoDTO>> obtenerEstadosPedidos() {
        List<PedidoEstadoDTO> estadosPedidos = pedidoService.obtenerEstadosPedidos();
        return ResponseEntity.ok(estadosPedidos);
    }
    @PutMapping("/{pedidoId}/confirmar-pago")
    public ResponseEntity<Map<String, Object>> confirmarPago(@PathVariable Long pedidoId) {
        Map<String, Object> response = pedidoService.confirmarPago(pedidoId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{pedidoId}/asignar-delivery/{usuarioId}")
    public ResponseEntity<Map<String, Object>> asignarDelivery(@PathVariable Long pedidoId, @PathVariable Long usuarioId) {
        Map<String, Object> response = pedidoService.asignarDelivery(pedidoId, usuarioId);
        return ResponseEntity.ok(response);
    }
}
