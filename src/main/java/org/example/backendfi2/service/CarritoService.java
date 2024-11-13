package org.example.backendfi2.service;

import org.example.backendfi2.dto.CarritoDTO;
import org.example.backendfi2.dto.ProductoEnCarritoDTO;
import org.example.backendfi2.model.*;
import org.example.backendfi2.repository.CarritoProductoRepository;
import org.example.backendfi2.repository.CarritoRepository;
import org.example.backendfi2.repository.ProductoRepository;
import org.example.backendfi2.repository.UsuarioRepository;
import org.example.backendfi2.repository.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@Service
public class CarritoService {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private CarritoRepository carritoRepository;

    @Autowired
    private CarritoProductoRepository carritoProductoRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;
    public CarritoDTO obtenerCarritoPorUsuarioId(Long usuarioId) {
        // Buscar el carrito del usuario con estado "REGISTRADO"
        Optional<Carrito> carritoOptional = carritoRepository.findByUsuarioIdAndEstado(usuarioId, "REGISTRADO");

        if (carritoOptional.isPresent()) {
            Carrito carrito = carritoOptional.get();
            List<ProductoEnCarritoDTO> productosDTO = new ArrayList<>();

            // Obtener los productos del carrito
            List<CarritoProducto> carritoProductos = carritoProductoRepository.findByCarritoId(carrito.getId());

            // Si existen productos asociados al carrito
            if (!carritoProductos.isEmpty()) {
                for (CarritoProducto cp : carritoProductos) {
                    // Obtener el producto relacionado
                    Optional<Producto> productoOptional = productoRepository.findById(cp.getProducto().getId());

                    if (productoOptional.isPresent()) {
                        Producto producto = productoOptional.get();

                        // Calcular el total por producto (precio * cantidad)
                        BigDecimal totalProd = producto.getPrecio().multiply(BigDecimal.valueOf(cp.getCantidad()));

                        // Agregar el producto al DTO
                        productosDTO.add(new ProductoEnCarritoDTO(
                                producto.getId(),
                                producto.getNombre(),
                                cp.getCantidad(),
                                totalProd,
                                producto.getPrecio()
                        ));
                    }
                }
            }

            // Devolver el carrito con sus productos
            return new CarritoDTO(carrito.getId(), productosDTO);
        } else {
            throw new RuntimeException("No se encontró un carrito registrado para el usuario con ID: " + usuarioId);
        }
    }


    public Carrito adicionarProductoACarrito(Long usuarioId, Long productoId, int cantidad) {
        try {
            // Buscar el usuario por ID
            Usuario usuario = usuarioRepository.findById(usuarioId)
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

            // Buscar el producto por ID
            Producto producto = productoRepository.findById(productoId)
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

            // Verificar si el usuario tiene un carrito en estado "REGISTRADO"
            Optional<Carrito> carritoOptional = carritoRepository.findByUsuarioIdAndEstado(usuarioId, "REGISTRADO");
            Carrito carrito;

            if (carritoOptional.isPresent()) {
                carrito = carritoOptional.get();
            } else {
                // Si no hay carrito, crear uno nuevo
                carrito = new Carrito();
                carrito.setActive(true);
                carrito.setCreatedAt(LocalDateTime.now());
                carrito.setCreatedBy(usuario.getUsuario());
                carrito.setEstado("REGISTRADO");
                carrito.setUsuario(usuario);
                carritoRepository.save(carrito);
            }

            // Verificar si el producto ya está en el carrito
            Optional<CarritoProducto> carritoProductoExistente = carritoProductoRepository
                    .findByCarritoIdAndProductoId(carrito.getId(), producto.getId());

            if (carritoProductoExistente.isPresent()) {
                // Si el producto ya existe en el carrito, actualizar la cantidad
                CarritoProducto carritoProducto = carritoProductoExistente.get();
                carritoProducto.setCantidad(carritoProducto.getCantidad() + cantidad);
                carritoProducto.setTotalProd(producto.getPrecio().multiply(new BigDecimal(carritoProducto.getCantidad())));
                carritoProductoRepository.save(carritoProducto);
            } else {
                // Si el producto no está en el carrito, añadirlo
                CarritoProducto carritoProducto = new CarritoProducto();
                carritoProducto.setCarrito(carrito);
                carritoProducto.setProducto(producto);
                carritoProducto.setCantidad(cantidad);
                carritoProducto.setTotalProd(producto.getPrecio().multiply(new BigDecimal(cantidad)));
                carritoProductoRepository.save(carritoProducto);
            }

            return carrito;  // Devolver el carrito actualizado
        } catch (RuntimeException e) {
            throw new RuntimeException("Error al añadir producto al carrito: " + e.getMessage(), e);
        }
    }
    public CarritoDTO eliminarProductoDeCarrito(Long usuarioId, Long productoId) {
        try {
            // Buscar el carrito del usuario en estado "REGISTRADO"
            Optional<Carrito> carritoOptional = carritoRepository.findByUsuarioIdAndEstado(usuarioId, "REGISTRADO");

            if (carritoOptional.isPresent()) {
                Carrito carrito = carritoOptional.get();

                // Verificar si el producto está en el carrito
                Optional<CarritoProducto> carritoProductoOptional = carritoProductoRepository.findByCarritoIdAndProductoId(carrito.getId(), productoId);

                if (carritoProductoOptional.isPresent()) {
                    CarritoProducto carritoProducto = carritoProductoOptional.get();

                    // Eliminar el producto del carrito
                    carritoProductoRepository.delete(carritoProducto);

                    // Devolver el carrito actualizado con los productos restantes
                    List<ProductoEnCarritoDTO> productosDTO = new ArrayList<>();
                    List<CarritoProducto> carritoProductos = carritoProductoRepository.findByCarritoId(carrito.getId());

                    for (CarritoProducto cp : carritoProductos) {
                        Optional<Producto> productoOptional = productoRepository.findById(cp.getProducto().getId());
                        if (productoOptional.isPresent()) {
                            Producto producto = productoOptional.get();
                            BigDecimal totalProd = producto.getPrecio().multiply(BigDecimal.valueOf(cp.getCantidad()));
                            productosDTO.add(new ProductoEnCarritoDTO(
                                    producto.getId(),
                                    producto.getNombre(),
                                    cp.getCantidad(),
                                    totalProd,
                                    producto.getPrecio()
                            ));
                        }
                    }

                    // Devolver el carrito actualizado
                    return new CarritoDTO(carrito.getId(), productosDTO);
                } else {
                    throw new RuntimeException("El producto no está en el carrito.");
                }
            } else {
                throw new RuntimeException("No se encontró un carrito registrado para el usuario con ID: " + usuarioId);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error al eliminar producto del carrito: " + e.getMessage(), e);
        }
    }



    @Transactional
    public Pedido concluirCarritoYRegistrarPedido(Long usuarioId) {
        try {
        // Verificar si el usuario tiene un carrito en estado REGISTRADO
        Carrito carrito = carritoRepository.findByUsuarioIdAndEstado(usuarioId, "REGISTRADO")
                .orElseThrow(() -> new RuntimeException("No se encontró un carrito en estado REGISTRADO para el usuario con ID: " + usuarioId));

        // Actualizar estado del carrito a CONCLUIDO
        carrito.setEstado("CONCLUIDO");
        carrito.setUpdatedAt(LocalDateTime.now());
        carritoRepository.save(carrito);

        // Calcular el total del pedido sumando total_prod de cada CarritoProducto en el carrito
        List<CarritoProducto> productosEnCarrito = carritoProductoRepository.findByCarritoId(carrito.getId());
        BigDecimal totalPedido = productosEnCarrito.stream()
                .map(CarritoProducto::getTotalProd)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Crear el nuevo pedido
        Pedido pedido = new Pedido();
        pedido.setCarrito(carrito);
        pedido.setUsuario(carrito.getUsuario());
        pedido.setTotal(totalPedido);
       // pedido.setDireccionEntrega(direccionEntrega);
        pedido.setEstado("REGISTRADO");  // Estado inicial del pedido
        pedido.setCreatedAt(LocalDateTime.now());
        pedido.setCreatedBy(carrito.getUsuario().getUsuario());

        // Guardar el pedido
        return pedidoRepository.save(pedido);
        } catch (RuntimeException e) {
            throw new RuntimeException("Error al concluir el carrito y registrar el pedido: " + e.getMessage(), e);
        }
    }




}

