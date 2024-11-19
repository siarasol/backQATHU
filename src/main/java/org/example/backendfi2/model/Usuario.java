package org.example.backendfi2.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter
@Entity
@Table(name = "usuarios", schema = "producto")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")

    private Long id;

    @Column(name = "active")
    private boolean active;


    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "updated_by")
    private String updatedBy;

    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "telefono")
    private String telefono;

    @Column(name = "direccion")
    private String direccion;

    @Column(name = "usuario")
    private String usuario;


    @Column(name = "paterno")
    private String paterno;

    @Column(name = "materno")
    private String materno;
    // Relación con la tabla roles
    @ManyToOne
    @JoinColumn(name = "id_rol", nullable = false)  // Clave foránea
    private Rol rol;

    @ManyToOne
    @JoinColumn(name = "id_comunidad", nullable = true) // Relación con Comunidad
    private Comunidad comunidad;

}