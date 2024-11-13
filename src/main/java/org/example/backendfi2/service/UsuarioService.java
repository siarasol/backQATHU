package org.example.backendfi2.service;
import java.util.*;
import java.time.Year;

import org.example.backendfi2.model.Rol;
import org.example.backendfi2.model.Usuario;
import org.example.backendfi2.repository.RolRepository;
import org.example.backendfi2.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.stream.Collectors;
import java.util.List;
import java.time.LocalDateTime;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private RolRepository rolRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<Usuario> findAllUsuarios() {
        return usuarioRepository.findAll(); // Esto debería devolver todos los usuarios
    }
    public Usuario saveUsuario(Usuario usuario) {
        try {
            // Asignar la fecha actual y activar el usuario
            usuario.setCreatedAt(LocalDateTime.now());
            usuario.setActive(true);

            // Asignar un rol al usuario
            // Aquí puedes buscar un rol específico. Por ejemplo, puedes buscar por ID o por nombre.
            // Si prefieres buscar por ID:
            Long rolId = 2L;  // ID del rol que deseas asignar (por ejemplo, Rol de "Usuario")
            Rol rol = rolRepository.findById(rolId).orElseThrow(() -> new Exception("Rol no encontrado"));
            usuario.setRol(rol);  // Asignar el rol al usuario

            // Guardar el usuario en la base de datos
            return usuarioRepository.save(usuario);

        } catch (Exception e) {
            // Manejo del error
            e.printStackTrace();
            throw new RuntimeException("Error al guardar el usuario: " + e.getMessage());
        }
    }


    public Map<String, Object> login(String usuario, String password) {
        Map<String, Object> response = new HashMap<>();

        try {
            // Buscar el usuario por su nombre
            Optional<Usuario> usuarioOpt = usuarioRepository.findByUsuario(usuario);
            System.out.println(passwordEncoder.encode(password));
            // Si el usuario existe



            if (usuarioOpt.isPresent()) {
                Usuario usuarioEncontrado = usuarioOpt.get();

                // Verificar la contraseña (asumimos que la contraseña está encriptada, usa BCrypt o similar)
                if (passwordEncoder.matches(password, usuarioEncontrado.getPassword())) {
                    response.put("success", true);
                    response.put("message", "Login exitoso");
                    response.put("response", usuarioEncontrado); // Agregamos los datos completos del usuario
                } else {
                    // Contraseña incorrecta
                    response.put("success", false);
                    response.put("message", "Contraseña incorrecta");
                }
            } else {
                // Usuario no encontrado
                response.put("success", false);
                response.put("message", "Usuario no encontrado");
            }
        } catch (Exception e) {
            // Manejamos cualquier excepción inesperada
            response.put("success", false);
            response.put("message", "Error en el servidor: " + e.getMessage());
        }

        return response;
    }


    public List<Usuario> listarUsuariosDelivery() {
        return usuarioRepository.findByRolId(4L); // 4L es el ID para el rol de delivery
    }

}