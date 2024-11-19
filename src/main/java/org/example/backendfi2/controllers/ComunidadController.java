package org.example.backendfi2.controllers;

import org.example.backendfi2.model.Comunidad;
import org.example.backendfi2.service.ComunidadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/comunidades")
public class ComunidadController {

    @Autowired
    private ComunidadService comunidadService;

    // Listar todas las comunidades
    @GetMapping
    public ResponseEntity<List<Comunidad>> getAllComunidades() {
        List<Comunidad> comunidades = comunidadService.getAllComunidades();
        return ResponseEntity.ok(comunidades);
    }

    // Registrar una nueva comunidad
    @PostMapping
    public ResponseEntity<Comunidad> createComunidad(@RequestBody Comunidad comunidad) {
        Comunidad nuevaComunidad = comunidadService.createComunidad(comunidad);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaComunidad);
    }

    // Eliminar una comunidad por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteComunidad(@PathVariable Long id) {
        try {
            comunidadService.deleteComunidad(id);
            return ResponseEntity.ok(Map.of("success", true, "message", "Comunidad eliminada exitosamente"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    Map.of("success", false, "message", "Error al eliminar la comunidad: " + e.getMessage())
            );
        }
    }
    // Editar una comunidad
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateComunidad(
            @PathVariable Long id,
            @RequestBody Comunidad comunidadActualizada
    ) {
        try {
            Comunidad comunidadEditada = comunidadService.updateComunidad(id, comunidadActualizada);
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Comunidad actualizada exitosamente",
                    "result", comunidadEditada
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "success", false,
                    "message", e.getMessage()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "success", false,
                    "message", "Error al actualizar la comunidad: " + e.getMessage()
            ));
        }
    }

}
