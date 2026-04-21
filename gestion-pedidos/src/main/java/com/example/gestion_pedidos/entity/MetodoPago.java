package com.example.gestion_pedidos.entity;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
@Entity
@Table(name = "METODO_PAGO", schema = "MICROS")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MetodoPago {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "metodo_pago_seq")
    @SequenceGenerator(name = "metodo_pago_seq", sequenceName = "MICROS.METODO_PAGO_SEQ", allocationSize = 1)
    @Column(name = "METODO_PAGO_ID")
    private Long metodoPagoId;

    @NotBlank(message = "El nombre del método de pago es obligatorio")
    @Size(max = 100, message = "El nombre no puede exceder 100 caracteres")
    @Column(name = "NOMBRE", nullable = false, length = 100)
    private String nombre;

    @Lob
    @Column(name = "DESCRIPCION")
    private String descripcion;

    @Column(name = "REQUIERE_VERIFICACION", length = 1)
    private String requiereVerificacion = "N";

    @DecimalMin(value = "0.0", message = "La comisión no puede ser negativa")
    @DecimalMax(value = "100.0", message = "La comisión no puede ser mayor a 100%")
    @Column(name = "COMISION_PORCENTAJE", precision = 5, scale = 2)
    private BigDecimal comisionPorcentaje = BigDecimal.ZERO;

    @Min(value = 0, message = "Los días de liquidación no pueden ser negativos")
    @Column(name = "DIAS_LIQUIDACION")
    private Integer diasLiquidacion = 0;

    @Column(name = "ACTIVO", length = 1)
    private String activo = "S";

    @CreationTimestamp
    @Column(name = "FECHA_CREACION")
    private LocalDateTime fechaCreacion;

    // Relación con Pedidos
    @OneToMany(mappedBy = "metodoPago", fetch = FetchType.LAZY)
    private List<Pedido> pedidos;

    // Métodos de conveniencia
    public boolean isActivo() {
        return "S".equals(this.activo);
    }

    public boolean requiereVerificacion() {
        return "S".equals(this.requiereVerificacion);
    }
}
