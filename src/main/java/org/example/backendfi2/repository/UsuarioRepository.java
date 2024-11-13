package org.example.backendfi2.repository;

import org.example.backendfi2.model.Rol;
import org.example.backendfi2.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByUsuario(String usuario);
    List<Usuario> findByRol(Rol rol);
    @Query("SELECT u FROM Usuario u WHERE u.rol.id = :rolId")
    List<Usuario> findByRolId(@Param("rolId") Long rolId);
}
