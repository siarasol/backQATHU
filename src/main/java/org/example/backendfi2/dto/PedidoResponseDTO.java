package org.example.backendfi2.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class PedidoResponseDTO {
    private Long id;
    private LocalDateTime createdAt;
    private String direccion;
    private BigDecimal total;
    private String estado;

    public PedidoResponseDTO(Long id, LocalDateTime createdAt, String direccion, BigDecimal total, String estado) {
        this.id = id;
        this.createdAt = createdAt;
        this.direccion = direccion;
        this.total = total;
        this.estado = estado;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
// Getters y Setters
}
