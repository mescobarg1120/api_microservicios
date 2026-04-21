package com.example.gestion_pedidos.entity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Entity
@Table(name = "DETALLE_PEDIDO", schema = "MICROS")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DetallePedido {

    @Id

    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "detalle_pedido_seq")
    @SequenceGenerator(name = "detalle_pedido_seq", sequenceName = "MICROS.DETALLE_PEDIDO_SEQ", allocationSize = 1)
    @Column(name = "DETALLE_ID")
    private Long detalleId;

    @JsonBackReference
    @NotNull(message = "El pedido es obligatorio")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PEDIDO_ID", nullable = false)
    private Pedido pedido;

    @NotNull(message = "El producto es obligatorio")
    @Column(name = "PRODUCTO_ID", nullable = false)
    private Long productoId;

    @NotNull(message = "La cantidad es obligatoria")
    @Min(value = 1, message = "La cantidad debe ser mayor a 0")
    @Column(name = "CANTIDAD", nullable = false)
    private Integer cantidad;

    @NotNull(message = "El precio unitario es obligatorio")
    @DecimalMin(value = "0.0", message = "El precio unitario no puede ser negativo")
    @Column(name = "PRECIO_UNITARIO", nullable = false, precision = 10, scale = 2)
    private BigDecimal precioUnitario;

    @DecimalMin(value = "0.0", message = "El descuento no puede ser negativo")
    @Column(name = "DESCUENTO_UNITARIO", precision = 10, scale = 2)
    private BigDecimal descuentoUnitario = BigDecimal.ZERO;

    // Calculado automáticamente por la base de datos (VIRTUAL COLUMN)
    @Column(name = "SUBTOTAL", precision = 12, scale = 2, insertable = false, updatable = false)
    private BigDecimal subtotal;

    @Column(name = "ES_PRODUCTO_ECOLOGICO", length = 1)
    private String esProductoEcologico = "S";

    @Size(max = 100, message = "La certificación no puede exceder 100 caracteres")
    @Column(name = "CERTIFICACION_APLICADA", length = 100)
    private String certificacionAplicada;

    @Min(value = 0, message = "Los puntos ecológicos no pueden ser negativos")
    @Column(name = "PUNTOS_ECOLOGICOS_ITEM")
    private Integer puntosEcologicosItem = 0;

    // Métodos de conveniencia
    public boolean isProductoEcologico() {
        return "S".equals(this.esProductoEcologico);
    }

    // Método para calcular subtotal manualmente si es necesario
    public BigDecimal calcularSubtotal() {
        return this.precioUnitario
                .subtract(this.descuentoUnitario)
                .multiply(BigDecimal.valueOf(this.cantidad));
    }

    // Método para calcular precio final por unidad
    public BigDecimal getPrecioFinalUnitario() {
        return this.precioUnitario.subtract(this.descuentoUnitario);
    }

    // =====================================================
    // ENUMS COMO CLASES INTERNAS ESTÁTICAS
    // =====================================================

    /**
     * Estados posibles de un pedido en el sistema
     */
    public static enum EstadoPedido {

        /**
         * Pedido recién creado, puede ser modificado
         */
        PENDIENTE,

        /**
         * Pedido confirmado por el cliente, stock reservado
         */
        CONFIRMADO,

        /**
         * Pedido en proceso de preparación
         */
        PROCESANDO,

        /**
         * Pedido enviado al cliente
         */
        ENVIADO,

        /**
         * Pedido entregado exitosamente
         */
        ENTREGADO,

        /**
         * Pedido cancelado por el cliente o el sistema
         */
        CANCELADO;

        /**
         * Verifica si el pedido puede ser modificado
         * @return true si se puede modificar, false en caso contrario
         */
        public boolean puedeSerModificado() {
            return this == PENDIENTE || this == CONFIRMADO;
        }

        /**
         * Verifica si el pedido está en estado final
         * @return true si está finalizado, false en caso contrario
         */
        public boolean esFinal() {
            return this == ENTREGADO || this == CANCELADO;
        }

        /**
         * Verifica si el pedido puede ser cancelado
         * @return true si se puede cancelar, false en caso contrario
         */
        public boolean puedeCancelarse() {
            return this != ENTREGADO && this != CANCELADO;
        }
    }

    /**
     * Tipos de entrega disponibles
     */
    public static enum TipoEntrega {

        /**
         * Entrega a domicilio
         */
        DOMICILIO,

        /**
         * Retiro en tienda
         */
        RETIRO_TIENDA,

        /**
         * Retiro en punto designado
         */
        PUNTO_RETIRO;

        /**
         * Verifica si requiere dirección de envío
         * @return true si necesita dirección, false en caso contrario
         */
        public boolean requiereDireccion() {
            return this == DOMICILIO;
        }
    }
}
