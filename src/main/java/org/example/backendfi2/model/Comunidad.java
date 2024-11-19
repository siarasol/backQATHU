package org.example.backendfi2.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "comunidad", schema = "producto")
public class Comunidad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @Column(name = "localidad", nullable = false, length = 150)
    private String localidad;

    @Column(name = "descripcion", columnDefinition = "TEXT")
    private String descripcion;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "created_by", length = 60)
    private String createdBy;

    @Column(name = "updated_by", length = 60)
    private String updatedBy;

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // Constructor vacío
    public Comunidad() {}

    // Constructor con parámetros
    public Comunidad(String nombre, String localidad, String descripcion, String createdBy) {
        this.nombre = nombre;
        this.localidad = localidad;
        this.descripcion = descripcion;
        this.createdBy = createdBy;
    }
}
