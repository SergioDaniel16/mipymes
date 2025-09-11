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
 * Entidad que representa cada Movimiento individual en un Asiento Contable
 * 
 * Cada movimiento es una línea de débito o crédito en el Libro Diario
 * que afecta a una cuenta específica.
 * 
 * Ejemplo del PDF "El Almacén El Planeador":
 * Asiento: "Depósito bancario por Q12,000.00"
 * Movimiento 1: Débito a Bancos Q12,000.00
 * Movimiento 2: Crédito a Efectivo en Caja Q12,000.00
 */
@Entity
@Table(name = "movimientos_contables")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MovimientoContable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Referencia al asiento contable al que pertenece este movimiento
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "asiento_contable_id", nullable = false)
    @NotNull(message = "El asiento contable es obligatorio")
    private AsientoContable asientoContable;

    /**
     * Cuenta afectada por este movimiento
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "cuenta_id", nullable = false)
    @NotNull(message = "La cuenta es obligatoria")
    private Cuenta cuenta;

    /**
     * Tipo de movimiento: DEBITO o CREDITO
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NotNull(message = "El tipo de movimiento es obligatorio")
    private TipoMovimiento tipoMovimiento;

    /**
     * Monto del movimiento
     * Siempre positivo, el tipo (débito/crédito) determina el efecto
     */
    @Column(nullable = false, precision = 15, scale = 2)
    @NotNull(message = "El monto es obligatorio")
    @Positive(message = "El monto debe ser positivo")
    private BigDecimal monto;

    /**
     * Descripción específica del movimiento (opcional)
     * Si no se proporciona, se usa la descripción del asiento
     */
    @Column(length = 500)
    private String descripcion;

    /**
     * Orden del movimiento dentro del asiento
     * Para mantener el orden de presentación
     */
    @Column(nullable = false)
    private Integer orden = 1;

    /**
     * Auditoría: cuándo se creó el movimiento
     */
    @Column(nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    /**
     * Auditoría: última modificación
     */
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
     * Método para obtener la descripción efectiva del movimiento
     */
    public String getDescripcionEfectiva() {
        return descripcion != null && !descripcion.trim().isEmpty() 
            ? descripcion 
            : asientoContable.getDescripcion();
    }

    /**
     * Método para verificar si es un débito
     */
    public boolean esDebito() {
        return tipoMovimiento == TipoMovimiento.DEBITO;
    }

    /**
     * Método para verificar si es un crédito
     */
    public boolean esCredito() {
        return tipoMovimiento == TipoMovimiento.CREDITO;
    }

    /**
     * Método para obtener el monto con signo según el tipo
     * Positivo para débitos, negativo para créditos (convención contable)
     */
    public BigDecimal getMontoConSigno() {
        return esDebito() ? monto : monto.negate();
    }

    /**
     * Tipos de movimiento contable
     */
    public enum TipoMovimiento {
        DEBITO("Débito"),
        CREDITO("Crédito");

        private final String descripcion;

        TipoMovimiento(String descripcion) {
            this.descripcion = descripcion;
        }

        public String getDescripcion() {
            return descripcion;
        }
    }
}