package org.example.backendfi2.dto;

public class ArchivoProductoDTO {
    private Long id;
    private String ruta;

    // Constructor
    public ArchivoProductoDTO(Long id, String ruta) {
        this.id = id;
        this.ruta = ruta;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRuta() {
        return ruta;
    }

    public void setRuta(String ruta) {
        this.ruta = ruta;
    }
}
