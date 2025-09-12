package edu.gt.umg.mipymes.contable.mipymes_contable_api.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entidad que registra los movimientos de inventario
 * (entradas, salidas, ajustes)
 */
@Entity
@Table(name = "movimientos_inventario")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MovimientoInventario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "producto_id", nullable = false)
    @NotNull(message = "El producto es obligatorio")
    private Producto producto;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NotNull(message = "El tipo de movimiento es obligatorio")
    private TipoMovimiento tipoMovimiento;

    @Column(nullable = false)
    @NotNull(message = "La cantidad es obligatoria")
    @Positive(message = "La cantidad debe ser positiva")
    private Integer cantidad;

    @Column(nullable = false, precision = 15, scale = 2)
    @NotNull(message = "El precio unitario es obligatorio")
    private BigDecimal precioUnitario;

    @Column(nullable = false)
    private Integer existenciaAnterior;

    @Column(nullable = false)
    private Integer existenciaNueva;

    @Column(length = 500)
    private String observaciones;

    @Column(length = 50)
    private String numeroDocumento; // Factura, recibo, etc.

    @Column(nullable = false, updatable = false)
    private LocalDateTime fechaMovimiento;

    @Column(length = 100)
    private String creadoPor;

    @PrePersist
    protected void onCreate() {
        fechaMovimiento = LocalDateTime.now();
    }

    /**
     * Calcular el valor total del movimiento
     */
    public BigDecimal getValorTotal() {
        return precioUnitario.multiply(new BigDecimal(cantidad));
    }

    /**
     * Tipos de movimiento de inventario
     */
    public enum TipoMovimiento {
        ENTRADA_COMPRA("Entrada por Compra"),
        SALIDA_VENTA("Salida por Venta"),
        AJUSTE_POSITIVO("Ajuste Positivo"),
        AJUSTE_NEGATIVO("Ajuste Negativo"),
        DEVOLUCION_CLIENTE("Devolución de Cliente"),
        DEVOLUCION_PROVEEDOR("Devolución a Proveedor");

        private final String descripcion;

        TipoMovimiento(String descripcion) {
            this.descripcion = descripcion;
        }

        public String getDescripcion() {
            return descripcion;
        }

        public boolean esEntrada() {
            return this == ENTRADA_COMPRA || this == AJUSTE_POSITIVO || this == DEVOLUCION_CLIENTE;
        }

        public boolean esSalida() {
            return this == SALIDA_VENTA || this == AJUSTE_NEGATIVO || this == DEVOLUCION_PROVEEDOR;
        }
    }
}