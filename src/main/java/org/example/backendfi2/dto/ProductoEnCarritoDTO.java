package org.example.backendfi2.dto;

import java.math.BigDecimal;

public class ProductoEnCarritoDTO {
    private Long idProducto;
    private String nombre;
    private int cantidad;
    private BigDecimal totalProd;
    private BigDecimal precio; // Añadido para el precio del producto

    public ProductoEnCarritoDTO(Long idProducto, String nombre, int cantidad, BigDecimal totalProd, BigDecimal precio) {
        this.idProducto = idProducto;
        this.nombre = nombre;
        this.cantidad = cantidad;
        this.totalProd = totalProd;
        this.precio = precio; // Inicializando el precio
    }

    // Getters y Setters
    public Long getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(Long idProducto) {
        this.idProducto = idProducto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public BigDecimal getTotalProd() {
        return totalProd;
    }

    public void setTotalProd(BigDecimal totalProd) {
        this.totalProd = totalProd;
    }

    public BigDecimal getPrecio() {
        return precio; // Getter para el precio
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio; // Setter para el precio
    }
}
