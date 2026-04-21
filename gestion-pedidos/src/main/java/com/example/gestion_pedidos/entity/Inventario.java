package com.example.gestion_pedidos.entity;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "INVENTARIO", schema = "MICROS")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Inventario {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "inventario_seq")
    @SequenceGenerator(name = "inventario_seq", sequenceName = "MICROS.INVENTARIO_SEQ", allocationSize = 1)
    @Column(name = "INVENTARIO_ID")
    private Long inventarioId;

    @NotNull(message = "El producto es obligatorio")
    @Column(name = "PRODUCTO_ID", nullable = false)
    private Long productoId;

    @NotNull(message = "La tienda es obligatoria")
    @Column(name = "TIENDA_ID", nullable = false)
    private Long tiendaId;

    @Min(value = 0, message = "La cantidad disponible no puede ser negativa")
    @Column(name = "CANTIDAD_DISPONIBLE")
    private Integer cantidadDisponible = 0;

    @Min(value = 0, message = "La cantidad reservada no puede ser negativa")
    @Column(name = "CANTIDAD_RESERVADA")
    private Integer cantidadReservada = 0;

    @Min(value = 0, message = "El nivel mínimo no puede ser negativo")
    @Column(name = "NIVEL_MINIMO")
    private Integer nivelMinimo = 0;

    @Min(value = 0, message = "El nivel óptimo no puede ser negativo")
    @Column(name = "NIVEL_OPTIMO")
    private Integer nivelOptimo = 0;

    @UpdateTimestamp
    @Column(name = "FECHA_ULTIMA_ACTUALIZACION")
    private LocalDateTime fechaUltimaActualizacion;

    // Métodos de conveniencia
    public Integer getCantidadReal() {
        return cantidadDisponible - cantidadReservada;
    }

    public boolean requiereReposicion() {
        return cantidadDisponible <= nivelMinimo;
    }

    public boolean hayStock(Integer cantidadSolicitada) {
        return getCantidadReal() >= cantidadSolicitada;
    }
}
