package org.example.backendfi2.service;
import java.util.*;
import java.time.Year;

import jakarta.mail.MessagingException;
import org.example.backendfi2.model.Comunidad;
import org.example.backendfi2.model.Rol;
import org.example.backendfi2.model.Usuario;
import org.example.backendfi2.repository.ComunidadRepository;
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

    @Autowired
    private EmailService emailService;

    @Autowired
    private ComunidadRepository comunidadRepository;

    public List<Usuario> findAllUsuarios() {
        return usuarioRepository.findAll(); // Esto debería devolver todos los usuarios
    }
    public Map<String, Object> saveUsuario(Usuario usuario) {
        try {
            // Verificar si el correo ya está registrado
            Optional<Usuario> usuarioExistente = usuarioRepository.findByEmail(usuario.getEmail());
            if (usuarioExistente.isPresent()) {
                return Map.of(
                        "success", false,
                        "message", "El correo ya está registrado. Por favor, usa otro correo."
                );
            }

            // Encriptar la contraseña antes de guardarla
            usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));

            // Asignar la fecha actual y activar el usuario
            usuario.setCreatedAt(LocalDateTime.now());
            usuario.setActive(true);

            // Asignar un rol al usuario
            if (usuario.getRol() == null || usuario.getRol().getId() == null) {
                return Map.of(
                        "success", false,
                        "message", "El rol del usuario es obligatorio"
                );
            }
            Rol rol = rolRepository.findById(usuario.getRol().getId())
                    .orElseThrow(() -> new Exception("Rol no encontrado"));
            usuario.setRol(rol);

            // Asignar comunidad al usuario si se proporciona
            if (usuario.getComunidad() != null && usuario.getComunidad().getId() != null) {
                Comunidad comunidad = comunidadRepository.findById(usuario.getComunidad().getId())
                        .orElseThrow(() -> new Exception("Comunidad no encontrada"));
                usuario.setComunidad(comunidad);
            }

            // Guardar el usuario en la base de datos
            Usuario usuarioGuardado = usuarioRepository.save(usuario);

            // Enviar correo de bienvenida
            enviarCorreoBienvenida(usuarioGuardado);

            return Map.of(
                    "success", true,
                    "message", "Usuario creado exitosamente",
                    "usuario", usuarioGuardado
            );

        } catch (Exception e) {
            e.printStackTrace();
            return Map.of(
                    "success", false,
                    "message", "Error al guardar el usuario: " + e.getMessage()
            );
        }
    }





    private void enviarCorreoBienvenida(Usuario usuario) {
        String asunto = "Bienvenido a QHATU BOLIVIA";
        String cuerpo = """
            <div style="border: 1px solid #cfcfcf; padding: 20px; font-family: Arial, sans-serif;">
                <h2 style="color: #28a745;">¡Bienvenido a QHATU BOLIVIA!</h2>
                <p>Estimado %s %s,</p>
                <p>Nos complace informarte que tu cuenta ha sido creada exitosamente.</p>
                <p>Ahora puedes iniciar sesión y comenzar a explorar nuestros productos y servicios. Te invitamos a disfrutar de una experiencia de compra única.</p>
                <p><strong>Gracias por unirte a nosotros,</strong></p>
                <p>El equipo de QHATU BOLIVIA</p>
                <p style="color: #6c757d; font-size: small;">Este es un mensaje automático. Por favor, no respondas a este correo.</p>
            </div>
            """.formatted(usuario.getNombre(), usuario.getPaterno());

        try {
            emailService.enviarCorreoPersonalizado(usuario.getEmail(), asunto, cuerpo);
        } catch (MessagingException e) {
            System.err.println("Error al enviar el correo de bienvenida: " + e.getMessage());
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