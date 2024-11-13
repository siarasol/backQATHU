package org.example.backendfi2.service;

import jakarta.mail.MessagingException;

import org.springframework.transaction.annotation.Transactional;
import org.example.backendfi2.model.*;
import org.example.backendfi2.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Service
public class PagoService {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private ArchivoRepository archivosRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RolRepository rolRepository;
    @Autowired
    private CarritoProductoRepository carritoProductoRepository;
    @Autowired
    private ProductoRepository productoRepository;
    @Autowired
    private EmailService emailService;
    @Autowired
    private PagoRepository pagosRepository;





    @Transactional
    public Map<String, Object> registrarPago(Long pedidoId, MultipartFile archivo, BigDecimal total, String tipo) {

        try {
            // Verificar si el pedido existe
            Optional<Pedido> pedidoOptional = pedidoRepository.findPedidoById(pedidoId);

            if (!pedidoOptional.isPresent()) {
                return Map.of("success", false, "message", "Pedido no encontrado");
            }

            // Crear y guardar el archivo
            String nombreArchivo = "comprobante_" + System.currentTimeMillis() + ".jpg";
            Archivos nuevoArchivo = new Archivos();
            nuevoArchivo.setNombre(nombreArchivo);
            nuevoArchivo.setTipoArchivo(archivo.getContentType());
            nuevoArchivo.setSize(archivo.getSize());
            nuevoArchivo.setPath("/u03/jboss/data/Qhatu/imagenes/" + nombreArchivo);
            nuevoArchivo.setCreatedAt(LocalDateTime.now());
            nuevoArchivo.setActive(true);

            // Guardar el archivo en el sistema de archivos
            File destinoArchivo = new File(nuevoArchivo.getPath());
            destinoArchivo.getParentFile().mkdirs(); // Crear directorio si no existe

            try (FileOutputStream fos = new FileOutputStream(destinoArchivo)) {
                fos.write(archivo.getBytes());
            } catch (IOException e) {
                System.err.println("Error al escribir el archivo: " + e.getMessage());
                return Map.of("success", false, "message", "Error al guardar el archivo en el sistema.");
            }

            // Guardar en la base de datos
            Archivos archivoGuardado = archivosRepository.save(nuevoArchivo);

            // Obtener el pedido y crear el objeto de pago
            Pedido pedido = pedidoOptional.get();

            Pago pago = new Pago();
            pago.setTotal(total);
            pago.setTipo(tipo);
            pago.setEstado("PAGADO");
            pago.setArchivo(archivoGuardado);
            pago.setCreatedAt(LocalDateTime.now());

            // Guardar el pago y asociarlo al pedido
            pago = pagosRepository.save(pago);
            pedido.setPago(pago);
            pedido.setEstado("PAGADO");
            pedidoRepository.save(pedido);

            // Actualizar el stock de productos en el carrito
            List<CarritoProducto> carritoProductos = carritoProductoRepository.findByCarritoId(pedido.getCarrito().getId());
            for (CarritoProducto carritoProducto : carritoProductos) {
                Producto producto = carritoProducto.getProducto();
                BigInteger stockActual = producto.getStock();
                int cantidadComprada = carritoProducto.getCantidad();

                if (stockActual.compareTo(BigInteger.valueOf(cantidadComprada)) >= 0) {
                    producto.setStock(stockActual.subtract(BigInteger.valueOf(cantidadComprada)));
                    productoRepository.save(producto);
                } else {
                    return Map.of("success", false, "message", "Stock insuficiente para el producto: " + producto.getNombre());
                }
            }

            // Enviar notificaciones a los administradores
            Rol rolAdmin = rolRepository.findById(1L).orElse(null);
            if (rolAdmin != null) {
                List<Usuario> administradores = usuarioRepository.findByRol(rolAdmin);
                String cuerpo = """
            <p>Estimado administrador,</p>
            <p>Un nuevo pago ha sido registrado exitosamente para el pedido. Favor de verificar y programar el delivery ID %d.</p>
            <p><strong>Detalles del pago:</strong></p>
            <ul>
                <li>Total: %s</li>
                <li>Tipo de Pago: %s</li>
            </ul>
            <p>Gracias por su atención.</p>
            """.formatted(pedidoId, total, tipo);

                for (Usuario admin : administradores) {
                    try {
                        emailService.enviarCorreoPersonalizado(admin.getEmail(), "Nuevo Pago Registrado - QHATU BOLIVIA", cuerpo);
                    } catch (MessagingException e) {
                        System.err.println("Error al enviar el correo a " + admin.getEmail() + ": " + e.getMessage());
                    }
                }
            }

            return Map.of("success", true, "message", "Pago registrado exitosamente, stock actualizado, y notificación enviada a administradores.");

        } catch (Exception e) {
            System.err.println("Error en registrarPago: " + e.getMessage());
            return Map.of("success", false, "message", "Error al registrar el pago: " + e.getMessage());
        }
    }

}
