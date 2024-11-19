package org.example.backendfi2.model;



import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "archivos", schema = "producto")
public class Archivos {

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

    @Column(name = "tipo_archivo", nullable = false, length = 150)
    private String tipoArchivo;



    @Column(name = "size")
    private Long size;

    @Column(name = "path", length = 200)
    private String path;

    // Getters y Setters

    public Archivos() {
    }

    public Archivos(Boolean active, LocalDateTime createdAt, String createdBy, LocalDateTime updatedAt, String updatedBy, String nombre, String tipoArchivo, Long size, String path) {
        this.active = active;
        this.createdAt = createdAt;
        this.createdBy = createdBy;
        this.updatedAt = updatedAt;
        this.updatedBy = updatedBy;
        this.nombre = nombre;
        this.tipoArchivo = tipoArchivo;

        this.size = size;
        this.path = path;
    }

    // Métodos adicionales si es necesario
}