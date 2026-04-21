package com.example.gestion_usuarios.service;
import com.example.gestion_usuarios.entity.Tienda;
import com.example.gestion_usuarios.repository.TiendaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TiendaService {

    private final TiendaRepository tiendaRepository;

    @Autowired
    public TiendaService(TiendaRepository tiendaRepository) {
        this.tiendaRepository = tiendaRepository;
    }

    @Transactional(readOnly = true)
    public List<Tienda> obtenerTodasLasTiendas() {
        return tiendaRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Tienda obtenerTiendaPorId(Long id) {
        return tiendaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tienda no encontrada con ID: " + id));
    }

    @Transactional(readOnly = true)
    public Tienda obtenerTiendaPorNombre(String nombre) {
        return tiendaRepository.findByNombre(nombre)
                .orElseThrow(() -> new RuntimeException("Tienda no encontrada con nombre: " + nombre));
    }

    @Transactional(readOnly = true)
    public List<Tienda> obtenerTiendasPorCiudad(String ciudad) {
        return tiendaRepository.findByCiudad(ciudad);
    }

    @Transactional(readOnly = true)
    public List<Tienda> obtenerTiendasPorRegion(String region) {
        return tiendaRepository.findByRegion(region);
    }

    @Transactional
    public Tienda crearTienda(Tienda tienda) {
        // Verificar si ya existe una tienda con el mismo nombre
        if (tiendaRepository.existsByNombre(tienda.getNombre())) {
            throw new RuntimeException("Ya existe una tienda con el nombre: " + tienda.getNombre());
        }

        // Establecer fechas
        tienda.setFechaCreacion(LocalDateTime.now());

        return tiendaRepository.save(tienda);
    }

    @Transactional
    public Tienda actualizarTienda(Long id, Tienda tiendaActualizada) {
        Tienda tienda = tiendaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tienda no encontrada con ID: " + id));

        // Verificar si el nuevo nombre ya existe y es diferente al actual
        if (!tienda.getNombre().equals(tiendaActualizada.getNombre()) &&
                tiendaRepository.existsByNombre(tiendaActualizada.getNombre())) {
            throw new RuntimeException("Ya existe una tienda con el nombre: " + tiendaActualizada.getNombre());
        }

        // Actualizar campos
        tienda.setNombre(tiendaActualizada.getNombre());
        tienda.setDireccion(tiendaActualizada.getDireccion());
        tienda.setCiudad(tiendaActualizada.getCiudad());
        tienda.setRegion(tiendaActualizada.getRegion());
        tienda.setHorarioApertura(tiendaActualizada.getHorarioApertura());
        tienda.setHorarioCierre(tiendaActualizada.getHorarioCierre());
        tienda.setTelefono(tiendaActualizada.getTelefono());
        tienda.setEmail(tiendaActualizada.getEmail());
        tienda.setFechaModificacion(LocalDateTime.now());

        return tiendaRepository.save(tienda);
    }

    @Transactional
    public void eliminarTienda(Long id) {
        if (!tiendaRepository.existsById(id)) {
            throw new RuntimeException("Tienda no encontrada con ID: " + id);
        }

        // Aquí podrías hacer verificaciones adicionales antes de eliminar
        // Por ejemplo, comprobar si hay usuarios asignados a esta tienda

        tiendaRepository.deleteById(id);
    }
}
