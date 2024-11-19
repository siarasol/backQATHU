package org.example.backendfi2.model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "categorias", schema = "producto")
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "active")
    private Boolean active;



    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "created_by", length = 60)
    private String createdBy;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "updated_by", length = 60)
    private String updatedBy;

    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;



    // Getters y Setters

    // Constructor vacío necesario para JPA
    public Categoria() {
    }

    // Constructor con parámetros (opcional)
    public Categoria(Boolean active, LocalDateTime createdAt, String createdBy, LocalDateTime updatedAt, String updatedBy, String nombre) {
        this.active = active;

        this.createdAt = createdAt;
        this.createdBy = createdBy;
        this.updatedAt = updatedAt;
        this.updatedBy = updatedBy;
        this.nombre = nombre;
    }

    // Métodos adicionales si es necesario
}