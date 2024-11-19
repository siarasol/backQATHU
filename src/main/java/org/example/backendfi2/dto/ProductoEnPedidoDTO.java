package org.example.backendfi2.dto;
import java.math.BigDecimal;

public class ProductoEnPedidoDTO {
    private Long productoId;
    private String nombreProducto;
    private BigDecimal precioUnitario;
    private int cantidad;
    private BigDecimal totalPorProducto;

    public ProductoEnPedidoDTO(Long productoId, String nombreProducto, BigDecimal precioUnitario, int cantidad, BigDecimal totalPorProducto) {
        this.productoId = productoId;
        this.nombreProducto = nombreProducto;
        this.precioUnitario = precioUnitario;
        this.cantidad = cantidad;
        this.totalPorProducto = totalPorProducto;
    }

    // Get
    //
    // ters y setters

    public Long getProductoId() {
        return productoId;
    }

    public void setProductoId(Long productoId) {
        this.productoId = productoId;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }

    public BigDecimal getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(BigDecimal precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public BigDecimal getTotalPorProducto() {
        return totalPorProducto;
    }

    public void setTotalPorProducto(BigDecimal totalPorProducto) {
        this.totalPorProducto = totalPorProducto;
    }
}
