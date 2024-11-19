package org.example.backendfi2.service;

import org.example.backendfi2.model.Comunidad;
import org.example.backendfi2.repository.ComunidadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ComunidadService {

    @Autowired
    private ComunidadRepository comunidadRepository;

    public Comunidad createComunidad(Comunidad comunidad) {
        return comunidadRepository.save(comunidad);
    }

    public List<Comunidad> getAllComunidades() {
        return comunidadRepository.findAll();
    }

    public Optional<Comunidad> getComunidadById(Long id) {
        return comunidadRepository.findById(id);
    }

    public void deleteComunidad(Long id) {
        comunidadRepository.deleteById(id);
    }

    public Comunidad updateComunidad(Long id, Comunidad comunidadActualizada) {
        return comunidadRepository.findById(id)
                .map(comunidad -> {
                    comunidad.setNombre(comunidadActualizada.getNombre());
                    comunidad.setLocalidad(comunidadActualizada.getLocalidad());
                    comunidad.setDescripcion(comunidadActualizada.getDescripcion());
                    comunidad.setUpdatedAt(LocalDateTime.now());
                    comunidad.setUpdatedBy(comunidadActualizada.getUpdatedBy());
                    return comunidadRepository.save(comunidad);
                })
                .orElseThrow(() -> new RuntimeException("Comunidad no encontrada con el ID: " + id));
    }

}
