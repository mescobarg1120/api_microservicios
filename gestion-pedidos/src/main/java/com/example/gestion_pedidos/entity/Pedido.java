package com.example.gestion_pedidos.entity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
@Entity
@Table(name = "PEDIDO", schema = "MICROS")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pedido_seq")
    @SequenceGenerator(name = "pedido_seq", sequenceName = "MICROS.PEDIDO_SEQ", allocationSize = 1)
    @Column(name = "PEDIDO_ID")
    private Long pedidoId;

    @Column(name = "NUMERO_PEDIDO", unique = true, length = 50)
    private String numeroPedido;

    @Column(name = "FECHA_PEDIDO", nullable = false)
    private LocalDate fechaPedido = LocalDate.now();

    @JsonIgnore
    @NotNull(message = "El cliente es obligatorio")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CLIENTE_ID", nullable = false)
    private Cliente cliente;


    @Column(name = "USUARIO_ID")
    private Long usuarioId;


    @Column(name = "TIENDA_ID")
    private Long tiendaId;

    @Enumerated(EnumType.STRING)
    @Column(name = "ESTADO", length = 20)
    private DetallePedido.EstadoPedido estado = DetallePedido.EstadoPedido.PENDIENTE;

    @JsonIgnore
    @NotNull(message = "El método de pago es obligatorio")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "METODO_PAGO_ID", nullable = false)
    private MetodoPago metodoPago;


    @NotNull(message = "El subtotal es obligatorio")
    @DecimalMin(value = "0.0", message = "El subtotal no puede ser negativo")
    @Column(name = "SUBTOTAL", nullable = false, precision = 12, scale = 2)
    private BigDecimal subtotal;

    @DecimalMin(value = "0.0", message = "El descuento no puede ser negativo")
    @Column(name = "DESCUENTO", precision = 12, scale = 2)
    private BigDecimal descuento = BigDecimal.ZERO;

    @DecimalMin(value = "0.0", message = "Los impuestos no pueden ser negativos")
    @Column(name = "IMPUESTOS", precision = 12, scale = 2)
    private BigDecimal impuestos = BigDecimal.ZERO;

    @NotNull(message = "El total es obligatorio")
    @DecimalMin(value = "0.0", message = "El total no puede ser negativo")
    @Column(name = "TOTAL", nullable = false, precision = 12, scale = 2)
    private BigDecimal total;

    @Size(max = 200, message = "La dirección de envío no puede exceder 200 caracteres")
    @Column(name = "DIRECCION_ENVIO", length = 200)
    private String direccionEnvio;

    @Size(max = 100, message = "La ciudad de envío no puede exceder 100 caracteres")
    @Column(name = "CIUDAD_ENVIO", length = 100)
    private String ciudadEnvio;

    @Size(max = 100, message = "La región de envío no puede exceder 100 caracteres")
    @Column(name = "REGION_ENVIO", length = 100)
    private String regionEnvio;

    @Enumerated(EnumType.STRING)
    @Column(name = "TIPO_ENTREGA", length = 50)
    private DetallePedido.TipoEntrega tipoEntrega = DetallePedido.TipoEntrega.DOMICILIO;

    @Column(name = "FECHA_ENTREGA_ESTIMADA")
    private LocalDate fechaEntregaEstimada;

    @Lob
    @Column(name = "NOTAS")
    private String notas;

    @Column(name = "ES_PEDIDO_ECOLOGICO", length = 1)
    private String esPedidoEcologico = "S";

    @Min(value = 0, message = "Los puntos ecológicos no pueden ser negativos")
    @Column(name = "PUNTOS_ECOLOGICOS")
    private Integer puntosEcologicos = 0;

    @CreationTimestamp
    @Column(name = "FECHA_CREACION")
    private LocalDateTime fechaCreacion;

    @UpdateTimestamp
    @Column(name = "FECHA_MODIFICACION")
    private LocalDateTime fechaModificacion;

    @JsonManagedReference
    // Relación con Detalles
    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<DetallePedido> detalles;

    // Métodos de conveniencia
    public boolean isPedidoEcologico() {
        return "S".equals(this.esPedidoEcologico);
    }

    public boolean isPendiente() {
        return DetallePedido.EstadoPedido.PENDIENTE.equals(this.estado);
    }

    public boolean isConfirmado() {
        return DetallePedido.EstadoPedido.CONFIRMADO.equals(this.estado);
    }

    public boolean puedeSerModificado() {
        return this.estado != null && this.estado.puedeSerModificado();
    }

    // Método para calcular totales
    public void calcularTotales() {
        if (detalles != null && !detalles.isEmpty()) {
            this.subtotal = detalles.stream()
                    .map(DetallePedido::getSubtotal)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            this.total = this.subtotal
                    .subtract(this.descuento)
                    .add(this.impuestos);
        }
    }

    // Métodos para trabajar con metodoPagoId
    @JsonProperty("metodoPagoId")
    public Long getMetodoPagoId() {
        return metodoPago != null ? metodoPago.getMetodoPagoId(): null;
    }

    @JsonProperty("metodoPagoId")
    public void setmetodoPagoId(Long metodopagoId) {
        this.metodoPago = null; // Limpiar la referencia actual
        if (metodopagoId != null) {
            MetodoPago newMetodoPago = new MetodoPago();
            newMetodoPago.setMetodoPagoId(metodopagoId);
            this.metodoPago = newMetodoPago;
        }
    }

    // Métodos para trabajar con clienteId
    @JsonProperty("clienteId")
    public Long getClienteId() {
        return cliente != null ? cliente.getClienteId(): null;
    }

    @JsonProperty("clienteId")
    public void setClienteId(Long clienteId) {
        this.cliente = null; // Limpiar la referencia actual
        if (clienteId != null) {
            Cliente newCliente = new Cliente();
            newCliente.setClienteId(clienteId);
            this.cliente = newCliente;
        }
    }
}
