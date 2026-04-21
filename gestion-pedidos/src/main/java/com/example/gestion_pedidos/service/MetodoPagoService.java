package com.example.gestion_pedidos.service;

import com.example.gestion_pedidos.entity.MetodoPago;
import com.example.gestion_pedidos.repository.MetodoPagoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class MetodoPagoService {

    private final MetodoPagoRepository metodoPagoRepository;

    public MetodoPago crearMetodoPago(MetodoPago metodoPago) {
        log.info("Creando método de pago: {}", metodoPago.getNombre());

        // Verificar nombre único
        if (metodoPagoRepository.findByNombreIgnoreCase(metodoPago.getNombre()).isPresent()) {
            throw new BusinessException("Ya existe un método de pago con ese nombre");
        }

        return metodoPagoRepository.save(metodoPago);
    }

    public List<MetodoPago> listarMetodosActivos() {
        return metodoPagoRepository.findByActivoOrderByNombre("S");
    }

    public MetodoPago obtenerMetodoPorId(Long metodoPagoId) {
        return metodoPagoRepository.findById(metodoPagoId)
                .orElseThrow(() -> new EntityNotFoundException("Método de pago no encontrado"));
    }

    public MetodoPago activarDesactivarMetodo(Long metodoPagoId, boolean activar) {
        MetodoPago metodo = obtenerMetodoPorId(metodoPagoId);
        metodo.setActivo(activar ? "S" : "N");
        return metodoPagoRepository.save(metodo);
    }
}

//
// excepciones
//

class BusinessException extends RuntimeException {
    public BusinessException(String message) {
        super(message);
    }
}

class EntityNotFoundException extends RuntimeException {
    public EntityNotFoundException(String message) {
        super(message);
    }
}
