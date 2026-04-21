package com.example.gestion_usuarios.service;

import com.example.gestion_usuarios.entity.Rol;
import com.example.gestion_usuarios.repository.RolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class RolService {

    private final RolRepository rolRepository;

    @Autowired
    public RolService(RolRepository rolRepository) {
        this.rolRepository = rolRepository;
    }

    @Transactional(readOnly = true)
    public List<Rol> obtenerTodosLosRoles() {
        return rolRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Rol obtenerRolPorId(Long id) {
        return rolRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Rol no encontrado con ID: " + id));
    }

    @Transactional(readOnly = true)
    public Rol obtenerRolPorNombre(String nombre) {
        return rolRepository.findByNombreDeRol(nombre)
                .orElseThrow(() -> new RuntimeException("Rol no encontrado con nombre: " + nombre));
    }

    @Transactional
    public Rol crearRol(Rol rol) {
        // Verificar si ya existe un rol con el mismo nombre
        if (rolRepository.existsByNombreDeRol(rol.getNombreDeRol())) {
            throw new RuntimeException("Ya existe un rol con el nombre: " + rol.getNombreDeRol());
        }

        // Establecer fechas
        rol.setFechaCreacion(LocalDateTime.now());

        return rolRepository.save(rol);
    }

    @Transactional
    public Rol actualizarRol(Long id, Rol rolActualizado) {
        Rol rol = rolRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Rol no encontrado con ID: " + id));

        // Verificar si el nuevo nombre ya existe y es diferente al actual
        if (!rol.getNombreDeRol().equals(rolActualizado.getNombreDeRol()) &&
                rolRepository.existsByNombreDeRol(rolActualizado.getNombreDeRol())) {
            throw new RuntimeException("Ya existe un rol con el nombre: " + rolActualizado.getNombreDeRol());
        }

        // Actualizar campos
        rol.setNombreDeRol(rolActualizado.getNombreDeRol());
        rol.setPermisos(rolActualizado.getPermisos());
        rol.setDescripcion(rolActualizado.getDescripcion());
        rol.setFechaModificacion(LocalDateTime.now());

        return rolRepository.save(rol);
    }

    @Transactional
    public void eliminarRol(Long id) {
        if (!rolRepository.existsById(id)) {
            throw new RuntimeException("Rol no encontrado con ID: " + id);
        }

        // Aquí podrías hacer verificaciones adicionales antes de eliminar
        // Por ejemplo, comprobar si hay usuarios que usan este rol

        rolRepository.deleteById(id);
    }
}
