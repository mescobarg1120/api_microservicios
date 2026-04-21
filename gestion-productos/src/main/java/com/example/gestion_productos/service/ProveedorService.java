package com.example.gestion_productos.service;
import com.example.gestion_productos.entity.Proveedor;
import com.example.gestion_productos.repository.ProveedorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ProveedorService {

    private final ProveedorRepository proveedorRepository;

    @Autowired
    public ProveedorService(ProveedorRepository proveedorRepository) {
        this.proveedorRepository = proveedorRepository;
    }

    // Guardar un nuevo proveedor
    public Proveedor guardar(Proveedor proveedor) {
        return proveedorRepository.save(proveedor);
    }

    // Actualizar un proveedor existente
    public Proveedor actualizar(Proveedor proveedor) {
        return proveedorRepository.save(proveedor);
    }

    // Buscar todos los proveedores
    @Transactional(readOnly = true)
    public List<Proveedor> buscarTodos() {
        return proveedorRepository.findAll();
    }

    // Buscar proveedores paginados
    @Transactional(readOnly = true)
    public Page<Proveedor> buscarTodosPaginados(Pageable pageable) {
        return proveedorRepository.findAll(pageable);
    }

    // Buscar proveedor por ID
    @Transactional(readOnly = true)
    public Optional<Proveedor> buscarPorId(Long id) {
        return proveedorRepository.findById(id);
    }

    // Buscar proveedor por RUT
    @Transactional(readOnly = true)
    public Optional<Proveedor> buscarPorRut(String rut) {
        return proveedorRepository.findByRut(rut);
    }

    // Buscar proveedores por nombre (parcial)
    @Transactional(readOnly = true)
    public List<Proveedor> buscarPorNombre(String nombre) {
        return proveedorRepository.findByNombreContainingIgnoreCase(nombre);
    }

    // Buscar proveedores por ciudad
    @Transactional(readOnly = true)
    public List<Proveedor> buscarPorCiudad(String ciudad) {
        return proveedorRepository.findByCiudad(ciudad);
    }

    // Buscar proveedores ecológicos
    @Transactional(readOnly = true)
    public List<Proveedor> buscarProveedoresEcologicos() {
        return proveedorRepository.findByEsEcologico("S");
    }

    // Buscar proveedores por tipo de producto
    @Transactional(readOnly = true)
    public List<Proveedor> buscarPorTipoProducto(String tipoProducto) {
        return proveedorRepository.findByTipoProductosContainingIgnoreCase(tipoProducto);
    }

    // Buscar proveedores por país de origen
    @Transactional(readOnly = true)
    public List<Proveedor> buscarPorPaisOrigen(String paisOrigen) {
        return proveedorRepository.findByPaisOrigen(paisOrigen);
    }

    // Eliminar un proveedor
    public void eliminar(Long id) {
        proveedorRepository.deleteById(id);
    }

    // Verificar si el proveedor tiene productos asociados
    @Transactional(readOnly = true)
    public boolean tieneProductosAsociados(Long proveedorId) {
        return proveedorRepository.existsByProveedorIdAndProductosProveedoresIsNotEmpty(proveedorId);
    }

}
