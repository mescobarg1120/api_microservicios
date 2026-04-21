package com.example.gestion_productos.service;
import com.example.gestion_productos.entity.Producto;
import com.example.gestion_productos.entity.ProductoProveedor;
import com.example.gestion_productos.entity.Proveedor;
import com.example.gestion_productos.repository.ProductoProveedorRepository;
import com.example.gestion_productos.repository.ProductoRepository;
import com.example.gestion_productos.repository.ProveedorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ProductoProveedorService {

    private final ProductoProveedorRepository productoProveedorRepository;
    private final ProductoRepository productoRepository;
    private final ProveedorRepository proveedorRepository;

    @Autowired
    public ProductoProveedorService(
            ProductoProveedorRepository productoProveedorRepository,
            ProductoRepository productoRepository,
            ProveedorRepository proveedorRepository) {
        this.productoProveedorRepository = productoProveedorRepository;
        this.productoRepository = productoRepository;
        this.proveedorRepository = proveedorRepository;
    }

    // Guardar una nueva relación producto-proveedor
    public ProductoProveedor guardar(ProductoProveedor productoProveedor) {
        return productoProveedorRepository.save(productoProveedor);
    }

    // Crear una nueva relación producto-proveedor con IDs
    public ProductoProveedor crearRelacion(Long productoId, Long proveedorId, BigDecimal precioCompra) {
        Optional<Producto> productoOpt = productoRepository.findById(productoId);
        Optional<Proveedor> proveedorOpt = proveedorRepository.findById(proveedorId);

        if (productoOpt.isPresent() && proveedorOpt.isPresent()) {
            Producto producto = productoOpt.get();
            Proveedor proveedor = proveedorOpt.get();

            ProductoProveedor productoProveedor = new ProductoProveedor(producto, proveedor);
            productoProveedor.setPrecioCompra(precioCompra);

            // Agregar a las colecciones de ambos lados
            producto.addProductoProveedor(productoProveedor);
            proveedor.addProductoProveedor(productoProveedor);

            return productoProveedorRepository.save(productoProveedor);
        }
        return null;
    }

    // Actualizar una relación existente
    public ProductoProveedor actualizar(ProductoProveedor productoProveedor) {
        return productoProveedorRepository.save(productoProveedor);
    }

    // Buscar todas las relaciones producto-proveedor
    @Transactional(readOnly = true)
    public List<ProductoProveedor> buscarTodas() {
        return productoProveedorRepository.findAll();
    }

    // Buscar relación por producto y proveedor
    @Transactional(readOnly = true)
    public Optional<ProductoProveedor> buscarPorProductoYProveedor(Long productoId, Long proveedorId) {
        return productoProveedorRepository.findByProductoProductoIdAndProveedorProveedorId(productoId, proveedorId);
    }

    // Buscar proveedores de un producto
    @Transactional(readOnly = true)
    public List<ProductoProveedor> buscarProveedoresPorProducto(Long productoId) {
        return productoProveedorRepository.findByProductoProductoId(productoId);
    }

    // Buscar productos de un proveedor
    @Transactional(readOnly = true)
    public List<ProductoProveedor> buscarProductosPorProveedor(Long proveedorId) {
        return productoProveedorRepository.findByProveedorProveedorId(proveedorId);
    }

    // Actualizar precio de compra
    public ProductoProveedor actualizarPrecioCompra(Long productoId, Long proveedorId, BigDecimal nuevoPrecioCompra) {
        Optional<ProductoProveedor> ppOpt = productoProveedorRepository.findByProductoProductoIdAndProveedorProveedorId(productoId, proveedorId);
        if (ppOpt.isPresent()) {
            ProductoProveedor pp = ppOpt.get();
            pp.actualizarPrecioCompra(nuevoPrecioCompra);
            return productoProveedorRepository.save(pp);
        }
        return null;
    }

    // Establecer como proveedor principal
    public ProductoProveedor establecerComoProveedorPrincipal(Long productoId, Long proveedorId) {
        // Primero quitar marca de principal a todos los proveedores del producto
        List<ProductoProveedor> proveedoresDelProducto = productoProveedorRepository.findByProductoProductoId(productoId);
        for (ProductoProveedor pp : proveedoresDelProducto) {
            pp.setEsProveedorPrincipal("N");
            productoProveedorRepository.save(pp);
        }

        // Establecer el nuevo proveedor principal
        Optional<ProductoProveedor> ppOpt = productoProveedorRepository.findByProductoProductoIdAndProveedorProveedorId(productoId, proveedorId);
        if (ppOpt.isPresent()) {
            ProductoProveedor pp = ppOpt.get();
            pp.establecerComoProveedorPrincipal();
            return productoProveedorRepository.save(pp);
        }
        return null;
    }

    // Registrar una compra
    public ProductoProveedor registrarCompra(Long productoId, Long proveedorId) {
        Optional<ProductoProveedor> ppOpt = productoProveedorRepository.findByProductoProductoIdAndProveedorProveedorId(productoId, proveedorId);
        if (ppOpt.isPresent()) {
            ProductoProveedor pp = ppOpt.get();
            pp.registrarCompra();
            return productoProveedorRepository.save(pp);
        }
        return null;
    }

    // Eliminar una relación producto-proveedor
    public void eliminar(Long productoId, Long proveedorId) {
        Optional<ProductoProveedor> ppOpt = productoProveedorRepository.findByProductoProductoIdAndProveedorProveedorId(productoId, proveedorId);
        ppOpt.ifPresent(productoProveedorRepository::delete);
    }
}
