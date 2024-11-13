package org.example.backendfi2.service;


import jakarta.mail.MessagingException;
import org.example.backendfi2.dto.PedidoEstadoDTO;
import org.example.backendfi2.model.*;
import org.example.backendfi2.dto.PedidoDetalleDTO;
import org.example.backendfi2.dto.ProductoEnPedidoDTO;
import org.example.backendfi2.dto.PedidoResponseDTO;
import org.example.backendfi2.repository.PagoRepository;
import org.example.backendfi2.repository.PedidoRepository;
import org.example.backendfi2.repository.UsuarioRepository;
import org.example.backendfi2.repository.DeliveryRepository;
import org.example.backendfi2.repository.CarritoProductoRepository;
import org.example.backendfi2.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.math.BigDecimal;
import java.util.ArrayList;

import java.util.Optional;

@Service
public class PedidoService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private PagoRepository pagosRepository;

    @Autowired
    private DeliveryRepository deliveryRepository;


    @Autowired
    private CarritoProductoRepository carritoProductoRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private EmailService emailService;

    public List<PedidoResponseDTO> listarPedidosPorUsuario(Long usuarioId) {
        try {
            List<Pedido> pedidos = pedidoRepository.findByUsuarioId(usuarioId);
            List<PedidoResponseDTO> pedidosDTO = new ArrayList<>();

            for (Pedido pedido : pedidos) {
                pedidosDTO.add(new PedidoResponseDTO(
                        pedido.getId(),
                        pedido.getCreatedAt(),
                        pedido.getDireccionEntrega(),
                        pedido.getTotal(),
                        pedido.getEstado()
                ));
            }

            return pedidosDTO;
        } catch (RuntimeException e) {
            throw new RuntimeException("Error al listar los pedidos: " + e.getMessage(), e);
        }
    }

    // Método para obtener detalle de un pedido específico
    public PedidoDetalleDTO obtenerDetallePedido(Long pedidoId) {
        Optional<Pedido> pedidoOptional = pedidoRepository.findById(pedidoId);
        if (pedidoOptional.isEmpty()) {
            throw new RuntimeException("Pedido no encontrado");
        }

        Pedido pedido = pedidoOptional.get();
        List<CarritoProducto> productosEnCarrito = carritoProductoRepository.findByCarritoId(pedido.getCarrito().getId());

        List<ProductoEnPedidoDTO> productosDTO = new ArrayList<>();
        BigDecimal totalCarrito = BigDecimal.ZERO;

        for (CarritoProducto cp : productosEnCarrito) {
            Optional<Producto> productoOptional = productoRepository.findById(cp.getProducto().getId());
            if (productoOptional.isPresent()) {
                Producto producto = productoOptional.get();
                BigDecimal totalPorProducto = producto.getPrecio().multiply(new BigDecimal(cp.getCantidad()));
                totalCarrito = totalCarrito.add(totalPorProducto);

                productosDTO.add(new ProductoEnPedidoDTO(
                        producto.getId(),
                        producto.getNombre(),
                        producto.getPrecio(),
                        cp.getCantidad(),
                        totalPorProducto
                ));
            }
        }

        return new PedidoDetalleDTO(pedido.getId(), totalCarrito, productosDTO);
    }

    @Value("${server.port:8080}")
    private String serverPort;

    public List<PedidoEstadoDTO> obtenerEstadosPedidos() {
        List<Pedido> pedidos = pedidoRepository.findAll();

        return pedidos.stream()
                .map(pedido -> {
                    Pago pago = pedido.getPago();
                    Archivos archivo = pago != null ? pago.getArchivo() : null;

                    // Construir URL completa para el archivo
                    String archivoUrl = archivo != null ? "http://localhost:" + serverPort + "/imagenes/" + archivo.getNombre() : null;

                    return new PedidoEstadoDTO(
                            pedido.getId(),
                            pedido.getEstado(),
                            pago != null ? pago.getEstado() : "SIN PAGO",
                            pago != null ? pago.getId() : null,
                            pago != null ? pago.getTotal() : null,
                            pago != null ? pago.getTipo() : null,
                            pago != null ? pago.getCreatedAt() : null,
                            archivo != null ? archivo.getId() : null,
                            archivo != null ? archivo.getNombre() : null,
                            archivoUrl, // URL de acceso al archivo
                            pedido.getDelivery() != null ? pedido.getDelivery().getEstado() : "SIN DELIVERY"
                    );
                })
                .collect(Collectors.toList());
    }



    @Transactional
    public Map<String, Object> confirmarPago(Long pedidoId) {
        // Buscar el pedido por su ID
        Optional<Pedido> pedidoOptional = pedidoRepository.findById(pedidoId);

        if (!pedidoOptional.isPresent()) {
            return Map.of("success", false, "message", "Pedido no encontrado");
        }

        Pedido pedido = pedidoOptional.get();
        Pago pago = pedido.getPago();

        // Verificar que el pedido tenga un pago asociado
        if (pago == null) {
            return Map.of("success", false, "message", "No hay un pago asociado a este pedido");
        }

        // Cambiar el estado del pago a CONFIRMADO
        pago.setEstado("CONFIRMADO");
        pagosRepository.save(pago); // Guardar el cambio en el estado del pago

        return Map.of("success", true, "message", "Estado del pago actualizado a CONFIRMADO");
    }






    @Transactional
    public Map<String, Object> asignarDelivery(Long pedidoId, Long usuarioId) {
        // Buscar el pedido por su ID
        Optional<Pedido> pedidoOptional = pedidoRepository.findById(pedidoId);
        if (!pedidoOptional.isPresent()) {
            return Map.of("success", false, "message", "Pedido no encontrado");
        }

        Pedido pedido = pedidoOptional.get();

        // Verificar si el pedido ya tiene un delivery asignado
        if (pedido.getDelivery() == null) {
            return Map.of("success", false, "message", "No se encontró un delivery asociado a este pedido");
        }

        Delivery delivery = pedido.getDelivery();

        // Buscar el usuario asignado para el delivery
        Optional<Usuario> usuarioOptional = usuarioRepository.findById(usuarioId);
        if (!usuarioOptional.isPresent()) {
            return Map.of("success", false, "message", "Usuario no encontrado");
        }

        Usuario usuario = usuarioOptional.get();

        // Actualizar el estado del pedido y del delivery
        pedido.setEstado("DELIVERY ASIGNADO");
        pedidoRepository.save(pedido);

        delivery.setEstado("ASIGNADO");
        delivery.setUsuario(usuario); // Asignar el usuario al delivery
        deliveryRepository.save(delivery);

        // Enviar notificación por correo al usuario de delivery asignado
        String cuerpo = """
            <p>Estimado %s,</p>
            <p>Se le ha asignado un nuevo pedido para entrega. Favor de revisar los detalles a continuación:</p>
            <p><strong>Detalles del Pedido:</strong></p>
            <ul>
                <li>ID del Pedido: %d</li>
                <li>Dirección de Entrega: %s</li>
                <li>Total del Pedido: %s</li>
                <li>Estado del Pedido: %s</li>
            </ul>
            <p>Gracias por su trabajo y compromiso.</p>
            <p>Atentamente,<br>Equipo QHATU BOLIVIA</p>
            """.formatted(usuario.getNombre(), pedidoId, pedido.getDireccionEntrega(), pedido.getTotal(), pedido.getEstado());

        try {
            emailService.enviarCorreoPersonalizado(usuario.getEmail(), "Nuevo Pedido Asignado - QHATU BOLIVIA", cuerpo);
        } catch (MessagingException e) {
            System.err.println("Error al enviar el correo al usuario de delivery " + usuario.getEmail() + ": " + e.getMessage());
        }

        return Map.of("success", true, "message", "Pedido y delivery asignados exitosamente al usuario, y correo enviado.");
    }



}
