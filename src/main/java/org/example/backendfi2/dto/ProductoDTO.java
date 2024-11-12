package org.example.backendfi2.dto;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

public class ProductoDTO {
    private Long id;
    private String nombre;

    public BigInteger getStock() {
        return stock;
    }

    public void setStock(BigInteger stock) {
        this.stock = stock;
    }

    private BigInteger stock;
    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    private String descripcion;

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    private BigDecimal precio;
    private List<ArchivoProductoDTO> archivos; // Usaremos un DTO para archivos también

    // Constructor
    public ProductoDTO(Long id, String nombre,BigInteger Stock,String descripcion, BigDecimal precio,List<ArchivoProductoDTO> archivos) {
        this.id = id;
        this.nombre = nombre;
        this.stock=Stock;
        this.descripcion=descripcion;
        this.archivos = archivos;

        this.precio=precio;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public List<ArchivoProductoDTO> getArchivos() {
        return archivos;
    }

    public void setArchivos(List<ArchivoProductoDTO> archivos) {
        this.archivos = archivos;
    }
}
