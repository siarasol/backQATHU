package org.example.backendfi2.repository;

import org.example.backendfi2.model.Archivos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArchivoRepository extends JpaRepository<Archivos, Long> {
}