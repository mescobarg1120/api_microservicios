package com.example.gestion_pedidos.service;
import com.example.gestion_pedidos.entity.*;
import com.example.gestion_pedidos.repository.*;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class InventarioService {

    private final InventarioRepository inventarioRepository;

    public Inventario crearInventario(Inventario inventario) {
        log.info("Creando inventario para producto {} en tienda {}",
                inventario.getProductoId(), inventario.getTiendaId());

        // Verificar que no exista ya
        if (inventarioRepository.existsByProductoIdAndTiendaId(
                inventario.getProductoId(), inventario.getTiendaId())) {
            throw new BusinessException("Ya existe inventario para este producto en esta tienda");
        }

        return inventarioRepository.save(inventario);
    }

    public Inventario obtenerInventario(Long productoId, Long tiendaId) {
        return inventarioRepository.findByProductoIdAndTiendaId(productoId, tiendaId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Inventario no encontrado para producto " + productoId + " en tienda " + tiendaId));
    }

    public List<Inventario> obtenerInventarioPorTienda(Long tiendaId) {
        return inventarioRepository.findByTiendaIdOrderByProductoId(tiendaId);
    }
    public boolean verificarDisponibilidad(Long productoId, Long tiendaId, Integer cantidadSolicitada) {
        return inventarioRepository.verificarStockDisponible(productoId, tiendaId, cantidadSolicitada)
                .isPresent();
    }

    /*

    public void reservarStock(Long productoId, Long tiendaId, Integer cantidad) {
        log.info("Reservando {} unidades del producto {} en tienda {}", cantidad, productoId, tiendaId);

        if (!verificarDisponibilidad(productoId, tiendaId, cantidad)) {
            throw new BusinessException("Stock insuficiente para reservar");
        }

        int filasAfectadas = inventarioRepository.reservarStock(productoId, tiendaId, cantidad);
        if (filasAfectadas == 0) {
            throw new BusinessException("No se pudo reservar el stock");
        }
    }

    public void liberarStockReservado(Long productoId, Long tiendaId, Integer cantidad) {
        log.info("Liberando {} unidades reservadas del producto {} en tienda {}", cantidad, productoId, tiendaId);
        inventarioRepository.liberarStockReservado(productoId, tiendaId, cantidad);
    }

    public void descontarStock(Long productoId, Long tiendaId, Integer cantidad) {
        log.info("Descontando {} unidades del producto {} en tienda {}", cantidad, productoId, tiendaId);

        int filasAfectadas = inventarioRepository.descontarStock(productoId, tiendaId, cantidad);
        if (filasAfectadas == 0) {
            throw new BusinessException("No se pudo descontar el stock");
        }
    }
    */
    public Inventario actualizarInventario(Long inventarioId, Inventario inventarioActualizado) {
        Inventario inventarioExistente = inventarioRepository.findById(inventarioId)
                .orElseThrow(() -> new EntityNotFoundException("Inventario no encontrado"));

        inventarioExistente.setCantidadDisponible(inventarioActualizado.getCantidadDisponible());
        inventarioExistente.setNivelMinimo(inventarioActualizado.getNivelMinimo());
        inventarioExistente.setNivelOptimo(inventarioActualizado.getNivelOptimo());

        return inventarioRepository.save(inventarioExistente);
    }
}
