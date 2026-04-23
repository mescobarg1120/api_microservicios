package com.example.gestion_pedidos.service;
import com.example.gestion_pedidos.entity.*;
import com.example.gestion_pedidos.repository.*;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;


import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ClienteService {

    private final ClienteRepository clienteRepository;
    private final PedidoRepository pedidoRepository;

    public Cliente crearCliente(Cliente cliente) {
        log.info("Creando nuevo cliente: {}", cliente.getEmail());

        // Validar email único
        if (clienteRepository.existsByEmail(cliente.getEmail())) {
            throw new BusinessException("Ya existe un cliente con el email: " + cliente.getEmail());
        }

        // Establecer valores por defecto
        if (cliente.getEstado() == null) {
            cliente.setEstado("ACTIVO");
        }
        if (cliente.getPrefiereEcologicos() == null) {
            cliente.setPrefiereEcologicos("S");
        }
        if (cliente.getTipoDeCliente() == null) {
            cliente.setTipoDeCliente("REGULAR");
        }

        Cliente clienteGuardado = clienteRepository.save(cliente);
        log.info("Cliente creado exitosamente con ID: {}", clienteGuardado.getClienteId());
        return clienteGuardado;
    }

    public Cliente actualizarCliente(Long clienteId, Cliente clienteActualizado) {
        log.info("Actualizando cliente ID: {}", clienteId);

        Cliente clienteExistente = obtenerClientePorId(clienteId);

        // Validar email único si cambió
        if (!clienteExistente.getEmail().equals(clienteActualizado.getEmail()) &&
                clienteRepository.existsByEmail(clienteActualizado.getEmail())) {
            throw new BusinessException("Ya existe un cliente con el email: " + clienteActualizado.getEmail());
        }

        // Actualizar campos
        clienteExistente.setNombre(clienteActualizado.getNombre());
        clienteExistente.setApellido(clienteActualizado.getApellido());
        clienteExistente.setEmail(clienteActualizado.getEmail());
        clienteExistente.setTelefono(clienteActualizado.getTelefono());
        clienteExistente.setDireccion(clienteActualizado.getDireccion());
        clienteExistente.setCiudad(clienteActualizado.getCiudad());
        clienteExistente.setRegion(clienteActualizado.getRegion());
        clienteExistente.setTipoDeCliente(clienteActualizado.getTipoDeCliente());
        clienteExistente.setPrefiereEcologicos(clienteActualizado.getPrefiereEcologicos());

        return clienteRepository.save(clienteExistente);
    }

    public Cliente obtenerClientePorId(Long clienteId) {
        return clienteRepository.findById(clienteId)
                .orElseThrow(() -> new EntityNotFoundException("Cliente no encontrado con ID: " + clienteId));
    }

    public Cliente obtenerClientePorEmail(String email) {
        return clienteRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Cliente no encontrado con email: " + email));
    }

    public List<Cliente> listarClientesActivos() {
        return clienteRepository.findByEstado("ACTIVO");
    }



    public void eliminarCliente(Long clienteId) {
        log.info("Eliminando cliente ID: {}", clienteId);

        Cliente cliente = obtenerClientePorId(clienteId);

        // Verificar si tiene pedidos
        List<Pedido> pedidos = pedidoRepository.findByClienteClienteIdOrderByFechaPedidoDesc(clienteId);
        if (!pedidos.isEmpty()) {
            // Solo desactivar si tiene pedidos
            cliente.setEstado("INACTIVO");
            clienteRepository.save(cliente);
            log.info("Cliente desactivado (tiene pedidos asociados)");
        } else {
            // Eliminar si no tiene pedidos
            clienteRepository.delete(cliente);
            log.info("Cliente eliminado completamente");
        }
    }

    public void actualizarTotalCompras(Long clienteId, BigDecimal montoCompra) {
        Cliente cliente = obtenerClientePorId(clienteId);
        cliente.setTotalDeCompras(cliente.getTotalDeCompras().add(montoCompra));
        clienteRepository.save(cliente);
    }
}
