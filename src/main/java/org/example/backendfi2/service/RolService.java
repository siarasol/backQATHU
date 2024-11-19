package org.example.backendfi2.service;


import org.example.backendfi2.model.Rol;
import org.example.backendfi2.repository.RolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RolService {

    @Autowired
    private RolRepository rolRepository;

    public List<Rol> findAllRoles() {
        return rolRepository.findAll();
    }
}