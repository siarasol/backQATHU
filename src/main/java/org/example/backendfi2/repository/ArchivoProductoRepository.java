package org.example.backendfi2.repository;

import org.example.backendfi2.model.ArchivoProducto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArchivoProductoRepository extends JpaRepository<ArchivoProducto, Long> {
}