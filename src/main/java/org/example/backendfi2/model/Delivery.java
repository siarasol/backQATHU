package org.example.backendfi2.model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.example.backendfi2.model.Usuario;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "deliverys", schema = "producto")
public class Delivery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "estado", nullable = false, length = 20)
    private String estado;

    @Column(name = "direccion", nullable = false, length = 255)
    private String direccion;

    @Column(name = "indicaciones", columnDefinition = "TEXT")
    private String indicaciones;

    @Column(name = "latitud", precision = 9, scale = 6)
    private BigDecimal latitud;

    @Column(name = "longitud", precision = 9, scale = 6)
    private BigDecimal longitud;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = true) // Usuario asignado al delivery
    private Usuario usuario;

    @PreUpdate
    public void setLastUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
