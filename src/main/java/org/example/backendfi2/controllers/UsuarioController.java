package org.example.backendfi2.controllers;

import org.example.backendfi2.model.Usuario;
import org.example.backendfi2.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {
    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    public ResponseEntity<List<Usuario>> getAllUsuarios() {
        try {
            List<Usuario> usuarios = usuarioService.findAllUsuarios();

            if (usuarios != null && !usuarios.isEmpty()) {
                return new ResponseEntity<>(usuarios, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT); // No se encontraron usuarios
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Endpoint para registrar un nuevo usuario
    @PostMapping
    public ResponseEntity<Map<String, Object>> createUsuario(@RequestBody Usuario usuario) {
        Map<String, Object> response = usuarioService.saveUsuario(usuario);

        if ((Boolean) response.get("success")) {
            return ResponseEntity.status(HttpStatus.CREATED).body(response); // 201 Created
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response); // 400 Bad Request
        }
    }
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(
            @RequestParam String usuario,
            @RequestParam String password) {

        // Llamamos al servicio para validar el login
        Map<String, Object> response = usuarioService.login(usuario, password);

        // Devolvemos la respuesta en formato JSON con código de estado OK
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/delivery")
    public ResponseEntity<List<Usuario>> listarUsuariosDelivery() {
        List<Usuario> usuariosDelivery = usuarioService.listarUsuariosDelivery();
        return ResponseEntity.ok(usuariosDelivery);
    }
}