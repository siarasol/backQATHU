package org.example.backendfi2.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "pagos", schema = "producto")
public class Pago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Asegúrate de que esta línea esté presente
    private Long id;


    @Column(name = "total", nullable = false)
    private BigDecimal total;

    @Column(name = "tipo", nullable = false)
    private String tipo;

    @ManyToOne
    @JoinColumn(name = "archivo_id")
    private Archivos archivo;

    @Column(name = "estado", nullable = false)
    private String estado;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();




    // Otros campos y métodos si es necesario
}