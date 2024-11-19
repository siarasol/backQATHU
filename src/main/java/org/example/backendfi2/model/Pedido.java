package org.example.backendfi2.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "pedido", schema = "producto")
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_carrito", nullable = false)
    private Carrito carrito;

    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @Column(nullable = false) // Asegúrate de que el total no sea nulo
    private BigDecimal total;

    @Column(name = "direccion_entrega")
    private String direccionEntrega;

    private String estado;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "updated_by")
    private String updatedBy;

    @ManyToOne // Si el pago puede ser opcional, cambia nullable a true
    @JoinColumn(name = "pago_id", nullable = true)
    private Pago pago;

    @OneToOne
    @JoinColumn(name = "delivery_id", nullable = true)
    private Delivery delivery;

    // Constructor, métodos adicionales, etc.
}
