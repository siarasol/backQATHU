package org.example.backendfi2.dto;



import java.math.BigDecimal;
import java.util.List;

public class PedidoDetalleDTO {
    private Long pedidoId;
    private BigDecimal totalCarrito;
    private List<ProductoEnPedidoDTO> productos;

    public PedidoDetalleDTO(Long pedidoId, BigDecimal totalCarrito, List<ProductoEnPedidoDTO> productos) {
        this.pedidoId = pedidoId;
        this.totalCarrito = totalCarrito;
        this.productos = productos;
    }

    public Long getPedidoId() {
        return pedidoId;
    }

    public void setPedidoId(Long pedidoId) {
        this.pedidoId = pedidoId;
    }

    public BigDecimal getTotalCarrito() {
        return totalCarrito;
    }

    public void setTotalCarrito(BigDecimal totalCarrito) {
        this.totalCarrito = totalCarrito;
    }

    public List<ProductoEnPedidoDTO> getProductos() {
        return productos;
    }

    public void setProductos(List<ProductoEnPedidoDTO> productos) {
        this.productos = productos;
    }
// Getters y setters
}
