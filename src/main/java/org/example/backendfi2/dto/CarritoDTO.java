package org.example.backendfi2.dto;

import java.math.BigDecimal;
import java.util.List;

public class CarritoDTO {
    private Long idCarrito;
    private List<ProductoEnCarritoDTO> productos;

    // Constructor
    public CarritoDTO(Long idCarrito, List<ProductoEnCarritoDTO> productos) {
        this.idCarrito = idCarrito;
        this.productos = productos;
    }

    // Getters y Setters
    public Long getIdCarrito() {
        return idCarrito;
    }

    public void setIdCarrito(Long idCarrito) {
        this.idCarrito = idCarrito;
    }

    public List<ProductoEnCarritoDTO> getProductos() {
        return productos;
    }

    public void setProductos(List<ProductoEnCarritoDTO> productos) {
        this.productos = productos;
    }
}
