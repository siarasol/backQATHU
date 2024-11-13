package org.example.backendfi2.repository;

import org.example.backendfi2.model.CarritoProducto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CarritoProductoRepository extends JpaRepository<CarritoProducto, Long> {

    List<CarritoProducto> findByCarritoId(Long carritoId);

    Optional<CarritoProducto> findByCarritoIdAndProductoId(Long carritoId, Long productoId);



}
