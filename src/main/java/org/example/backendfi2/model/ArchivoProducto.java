package org.example.backendfi2.model;


import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "archivo_producto", schema = "producto")
public class ArchivoProducto {

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_archivo", nullable = false)
    private Archivos archivos;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_productos", nullable = false)
    @JsonBackReference
    private Producto producto;

    // Getters y Setters

    public ArchivoProducto() {
    }

    public ArchivoProducto(Boolean active, LocalDateTime createdAt, String createdBy, LocalDateTime updatedAt, String updatedBy, Archivos archivos, Producto producto) {
        this.active = active;
        this.createdAt = createdAt;
        this.createdBy = createdBy;
        this.updatedAt = updatedAt;
        this.updatedBy = updatedBy;
        this.archivos = archivos;
        this.producto = producto;
    }

    // Métodos adicionales si es necesario
}
