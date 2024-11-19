package org.example.backendfi2.controllers;

import org.example.backendfi2.dto.ApiResponse;
import org.example.backendfi2.dto.CarritoDTO;
import org.example.backendfi2.model.Carrito;
import org.example.backendfi2.model.Pedido;
import org.example.backendfi2.service.CarritoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/carrito")
public class CarritoController {

    @Autowired
    private CarritoService carritoService;

    @PostMapping("/adicionar")
    public ResponseEntity<Map<String, Object>> adicionarProducto(@RequestBody Map<String, Object> request) {
        Long usuarioId = Long.valueOf(request.get("usuarioId").toString());
        Long productoId = Long.valueOf(request.get("productoId").toString());
        int cantidad = Integer.parseInt(request.get("cantidad").toString());

        try {
            carritoService.adicionarProductoACarrito(usuarioId, productoId, cantidad);
            return ResponseEntity.ok(Map.of("success", true, "message", "Producto añadido al carrito con éxito."));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("success", false, "message", "Error al añadir producto al carrito: " + e.getMessage()));
        }
    }
    @GetMapping("/detalles/{usuarioId}")
    public ResponseEntity<Map<String, Object>> obtenerDetallesCarrito(@PathVariable Long usuarioId) {
        try {
            CarritoDTO carritoDTO = carritoService.obtenerCarritoPorUsuarioId(usuarioId);
            return ResponseEntity.ok(Map.of("success", true, "message", "Carrito encontrado", "carrito", carritoDTO));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("success", false, "message", e.getMessage()));
        }
    }

    @DeleteMapping("/eliminar")
    public ResponseEntity<Map<String, Object>> eliminarProductoDelCarrito(@RequestBody Map<String, Object> request) {
        Long usuarioId = Long.parseLong(request.get("usuarioId").toString());
        Long productoId = Long.parseLong(request.get("productoId").toString());

        try {
            CarritoDTO carritoDTO = carritoService.eliminarProductoDeCarrito(usuarioId, productoId);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Producto eliminado del carrito");
            response.put("result", carritoDTO);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @PostMapping("/concluir")
    public ResponseEntity<Map<String, Object>> concluirCarritoYRegistrarPedido(
            @RequestParam Long usuarioId
            ) {

        Map<String, Object> response = new HashMap<>();

        try {
            carritoService.concluirCarritoYRegistrarPedido(usuarioId);
            response.put("message", "Pedido creado exitosamente");
            response.put("success", true);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            response.put("message", "Error al crear el pedido: " + e.getMessage());
            response.put("success", false);
            return ResponseEntity.badRequest().body(response);
        }
    }

}
