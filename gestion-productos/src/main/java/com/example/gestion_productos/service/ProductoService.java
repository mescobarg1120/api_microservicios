package com.example.gestion_productos.service;

import com.example.gestion_productos.entity.Producto;
import com.example.gestion_productos.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ProductoService {

    private final ProductoRepository productoRepository;

    @Autowired
    public ProductoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    // Guardar un nuevo producto
    public Producto guardar(Producto producto) {
        // Si es un producto nuevo, calcular el margen de ganancia automáticamente
        if (producto.getProductoId() == null) {
            producto.calcularMargenGanancia();
        }
        return productoRepository.save(producto);
    }

    // Actualizar un producto existente
    public Producto actualizar(Producto producto) {
        return productoRepository.save(producto);
    }

    // Buscar todos los productos
    @Transactional(readOnly = true)
    public List<Producto> buscarTodos() {
        return productoRepository.findAll();
    }

    // Buscar productos paginados
    @Transactional(readOnly = true)
    public List<Producto> obtenerTodosLosProductos() {
        return productoRepository.findAll();
    }

    // Buscar producto por ID
    @Transactional(readOnly = true)
    public Optional<Producto> buscarPorId(Long id) {
        return productoRepository.findById(id);
    }

    // Buscar producto por código SKU
    @Transactional(readOnly = true)
    public Optional<Producto> buscarPorCodigoSku(String codigoSku) {
        return productoRepository.findByCodigoSku(codigoSku);
    }

    // Buscar productos por categoría
    @Transactional(readOnly = true)
    public List<Producto> buscarPorCategoria(Long categoriaId) {
        return productoRepository.findByCategoriaCategoriaId(categoriaId);
    }

    // Buscar productos por nombre (parcial)
    @Transactional(readOnly = true)
    public List<Producto> buscarPorNombre(String nombre) {
        return productoRepository.findByNombreProductoContainingIgnoreCase(nombre);
    }

    // Buscar productos por rango de precio
    @Transactional(readOnly = true)
    public List<Producto> buscarPorRangoPrecio(BigDecimal precioMin, BigDecimal precioMax) {
        return productoRepository.findByPrecioUnitarioBetween(precioMin, precioMax);
    }

    // Buscar productos ecológicos
    @Transactional(readOnly = true)
    public List<Producto> buscarProductosEcologicos() {
        return productoRepository.findByEsEcologico("S");
    }

    // Buscar productos por proveedor
    @Transactional(readOnly = true)
    public List<Producto> buscarPorProveedor(Long proveedorId) {
        return productoRepository.findByProveedorProveedorId(proveedorId);
    }

    // Eliminar un producto
    public void eliminar(Long id) {
        productoRepository.deleteById(id);
    }

    // Actualizar el precio de venta y recalcular el margen
    public Producto actualizarPrecioVenta(Long productoId, BigDecimal nuevoPrecio) {
        Optional<Producto> productoOpt = productoRepository.findById(productoId);
        if (productoOpt.isPresent()) {
            Producto producto = productoOpt.get();
            producto.setPrecioUnitario(nuevoPrecio);
            producto.calcularMargenGanancia();
            return productoRepository.save(producto);
        }
        return null;
    }

    // Actualizar el precio de compra y recalcular el margen
    public Producto actualizarPrecioCompra(Long productoId, BigDecimal nuevoPrecioCompra) {
        Optional<Producto> productoOpt = productoRepository.findById(productoId);
        if (productoOpt.isPresent()) {
            Producto producto = productoOpt.get();
            producto.setPrecioCompra(nuevoPrecioCompra);
            producto.calcularMargenGanancia();
            return productoRepository.save(producto);
        }
        return null;
    }
}
