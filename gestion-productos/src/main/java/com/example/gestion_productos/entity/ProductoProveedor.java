package com.example.gestion_productos.entity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "producto_proveedor")
@IdClass(ProductoProveedor.ProductoProveedorId.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductoProveedor {

    @Id
    @ManyToOne
    @JoinColumn(name = "producto_id")
    private Producto producto;

    @Id
    @ManyToOne
    @JoinColumn(name = "proveedor_id")
    private Proveedor proveedor;

    @Size(max = 50, message = "El código de producto del proveedor no debe exceder los 50 caracteres")
    @Column(name = "codigo_producto_proveedor", length = 50)
    private String codigoProductoProveedor;

    @NotNull(message = "El precio de compra es obligatorio")
    @Positive(message = "El precio de compra debe ser mayor que cero")
    @Column(name = "precio_compra", precision = 10, scale = 2, nullable = false)
    private BigDecimal precioCompra;

    @Column(name = "tiempo_entrega_dias")
    private Integer tiempoEntregaDias;

    @Column(name = "cantidad_minima")
    private Integer cantidadMinima;

    @Column(name = "es_proveedor_principal", length = 1)
    private String esProveedorPrincipal;

    @Column(name = "es_proveedor_eco", length = 1)
    private String esProveedorEco;

    @Size(max = 100, message = "La certificación del proveedor no debe exceder los 100 caracteres")
    @Column(name = "certificacion_proveedor", length = 100)
    private String certificacionProveedor;

    @Column(name = "ultima_compra")
    private Date ultimaCompra;

    // Constructor para facilitar la creación - Versión ajustada
    public ProductoProveedor(Producto producto, Proveedor proveedor) {
        this.producto = producto;
        this.proveedor = proveedor;
    }

    // Método para actualizar el precio de compra
    public void actualizarPrecioCompra(BigDecimal nuevoPrecioCompra) {
        this.precioCompra = nuevoPrecioCompra;
        if (this.esProveedorPrincipal != null && this.esProveedorPrincipal.equals("S")) {
            // Si es el proveedor principal, actualizar el precio de compra en el producto
            if (this.producto != null) {
                this.producto.setPrecioCompra(nuevoPrecioCompra);
                this.producto.calcularMargenGanancia();
            }
        }
    }

    // Método para registrar una nueva compra - Versión ajustada
    public void registrarCompra() {
        // Convertir LocalDate a java.util.Date
        this.ultimaCompra = java.sql.Date.valueOf(LocalDate.now());

        // Si es el proveedor principal, también actualizar la última compra en el producto
        if (this.esProveedorPrincipal != null && this.esProveedorPrincipal.equals("S") && this.producto != null) {
            this.producto.setUltimaCompra(this.ultimaCompra);
        }
    }

    // Método para establecer como proveedor principal - Versión ajustada
    public void establecerComoProveedorPrincipal() {
        this.esProveedorPrincipal = "S";
        if (this.producto != null && this.proveedor != null) {
            // Establecer este proveedor como el principal para el producto
            this.producto.setProveedor(this.proveedor);
            this.producto.setPrecioCompra(this.precioCompra);
            this.producto.calcularMargenGanancia();
        }
    }

    // Clase interna para representar la clave primaria compuesta - Versión ajustada
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProductoProveedorId implements Serializable {
        private Long producto;  // Debe coincidir con el nombre del campo en la entidad
        private Long proveedor; // Debe coincidir con el nombre del campo en la entidad

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ProductoProveedorId that = (ProductoProveedorId) o;
            return Objects.equals(producto, that.producto) &&
                    Objects.equals(proveedor, that.proveedor);
        }

        @Override
        public int hashCode() {
            return Objects.hash(producto, proveedor);
        }
    }
}
