package edu.gt.umg.mipymes.contable.mipymes_contable_api.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Entidad que representa un producto en el inventario
 * Para "El Almacén El Planeador" - control de mercaderías
 */
@Entity
@Table(name = "productos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 20)
    @NotBlank(message = "El código del producto es obligatorio")
    private String codigo;

    @Column(nullable = false, length = 200)
    @NotBlank(message = "El nombre del producto es obligatorio")
    private String nombre;

    @Column(length = 500)
    private String descripcion;

    @Column(nullable = false, precision = 15, scale = 2)
    @NotNull(message = "El precio de compra es obligatorio")
    @PositiveOrZero(message = "El precio de compra debe ser mayor o igual a cero")
    private BigDecimal precioCompra;

    @Column(nullable = false, precision = 15, scale = 2)
    @NotNull(message = "El precio de venta es obligatorio")
    @PositiveOrZero(message = "El precio de venta debe ser mayor o igual a cero")
    private BigDecimal precioVenta;

    @Column(nullable = false)
    @NotNull(message = "La cantidad en existencia es obligatoria")
    @PositiveOrZero(message = "La existencia debe ser mayor o igual a cero")
    private Integer existencia = 0;

    @Column(nullable = false)
    @PositiveOrZero(message = "El stock mínimo debe ser mayor o igual a cero")
    private Integer stockMinimo = 0;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UnidadMedida unidadMedida;

    @Column(nullable = false)
    private Boolean activo = true;

    @Column(length = 100)
    private String categoria;

    @Column(length = 100)
    private String proveedor;

    @Column(nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    @Column(nullable = false)
    private LocalDateTime fechaModificacion;

    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        fechaCreacion = now;
        fechaModificacion = now;
    }

    @PreUpdate
    protected void onUpdate() {
        fechaModificacion = LocalDateTime.now();
    }

    /**
     * Calcular el valor del inventario de este producto
     */
    public BigDecimal getValorInventario() {
        return precioCompra.multiply(new BigDecimal(existencia));
    }

    /**
     * Verificar si el producto está en stock mínimo
     */
    public boolean estaEnStockMinimo() {
        return existencia <= stockMinimo;
    }

    /**
     * Calcular margen de ganancia
     */
    public BigDecimal getMargenGanancia() {
        if (precioCompra.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        return precioVenta.subtract(precioCompra).divide(precioCompra, 4, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * Enum para unidades de medida
     */
    public enum UnidadMedida {
        UNIDAD("Unidad"),
        CAJA("Caja"),
        PAQUETE("Paquete"),
        KILOGRAMO("Kilogramo"),
        LITRO("Litro"),
        METRO("Metro"),
        DOCENA("Docena");

        private final String descripcion;

        UnidadMedida(String descripcion) {
            this.descripcion = descripcion;
        }

        public String getDescripcion() {
            return descripcion;
        }
    }
}