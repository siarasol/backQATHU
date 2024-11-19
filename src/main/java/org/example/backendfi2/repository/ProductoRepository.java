package org.example.backendfi2.repository;

import org.example.backendfi2.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public interface ProductoRepository extends JpaRepository<Producto, Long> {
    // Método para buscar productos por createdBy
    List<Producto> findByCreatedBy(String createdBy);
}
