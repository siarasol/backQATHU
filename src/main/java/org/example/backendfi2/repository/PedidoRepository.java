package org.example.backendfi2.repository;

import org.example.backendfi2.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    List<Pedido> findByUsuarioId(Long usuarioId);

    @Query("SELECT p FROM Pedido p WHERE p.id = :pedidoId")
    Optional<Pedido> findPedidoById(@Param("pedidoId") Long pedidoId);
}

