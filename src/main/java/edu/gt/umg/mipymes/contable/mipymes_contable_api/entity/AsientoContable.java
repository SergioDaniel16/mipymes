package edu.gt.umg.mipymes.contable.mipymes_contable_api.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;

/**
 * Entidad que representa un Asiento Contable en el Libro Diario
 * 
 * Cada asiento contable agrupa varios movimientos (débitos y créditos)
 * que representan una transacción comercial completa.
 * 
 * Ejemplo del PDF "El Almacén El Planeador":
 * Asiento: "Depósito bancario por Q12,000.00"
 * - Débito: Bancos Q12,000.00
 * - Crédito: Efectivo en Caja Q12,000.00
 */
@Entity
@Table(name = "asientos_contables")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AsientoContable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Número correlativo del asiento en el libro diario
     * Ejemplo: 1, 2, 3, etc.
     */
    @Column(nullable = false, unique = true)
    @NotNull(message = "El número de asiento es obligatorio")
    private Integer numeroAsiento;

    /**
     * Fecha de la transacción
     */
    @Column(nullable = false)
    @NotNull(message = "La fecha del asiento es obligatoria")
    private LocalDate fecha;

    /**
     * Descripción de la transacción
     * Ejemplo: "Depósito bancario por Q12,000.00"
     */
    @Column(nullable = false, length = 500)
    @NotBlank(message = "La descripción del asiento es obligatoria")
    private String descripcion;

    /**
     * Referencia externa (número de factura, recibo, etc.)
     */
    @Column(length = 50)
    private String referencia;

    /**
     * Tipo de asiento para clasificación
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NotNull(message = "El tipo de asiento es obligatorio")
    private TipoAsiento tipo;

    /**
     * Total de débitos del asiento
     * Debe ser igual al total de créditos (partida doble)
     */
    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal totalDebitos = BigDecimal.ZERO;

    /**
     * Total de créditos del asiento
     * Debe ser igual al total de débitos (partida doble)
     */
    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal totalCreditos = BigDecimal.ZERO;

    /**
     * Estado del asiento
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoAsiento estado = EstadoAsiento.BORRADOR;

    /**
     * Lista de movimientos que componen este asiento
     * Un asiento tiene múltiples movimientos (débitos y créditos)
     */
    @OneToMany(mappedBy = "asientoContable", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<MovimientoContable> movimientos = new ArrayList<>();

    /**
     * Auditoría: cuándo se creó el asiento
     */
    @Column(nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    /**
     * Auditoría: última modificación
     */
    @Column(nullable = false)
    private LocalDateTime fechaModificacion;

    /**
     * Usuario que creó el asiento (opcional por ahora)
     */
    @Column(length = 100)
    private String creadoPor;

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
     * Método para validar que el asiento cumple la partida doble
     */
    public boolean estaBalanceado() {
        return totalDebitos.compareTo(totalCreditos) == 0;
    }

    /**
     * Método para calcular totales basado en los movimientos
     */
    public void calcularTotales() {
        totalDebitos = movimientos.stream()
            .filter(m -> m.getTipoMovimiento() == MovimientoContable.TipoMovimiento.DEBITO)
            .map(MovimientoContable::getMonto)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        totalCreditos = movimientos.stream()
            .filter(m -> m.getTipoMovimiento() == MovimientoContable.TipoMovimiento.CREDITO)
            .map(MovimientoContable::getMonto)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * Tipos de asientos contables
     */
    public enum TipoAsiento {
        APERTURA("Asiento de Apertura"),
        OPERACION("Operación Normal"),
        AJUSTE("Asiento de Ajuste"),
        CIERRE("Asiento de Cierre");

        private final String descripcion;

        TipoAsiento(String descripcion) {
            this.descripcion = descripcion;
        }

        public String getDescripcion() {
            return descripcion;
        }
    }

    /**
     * Estados del asiento contable
     */
    public enum EstadoAsiento {
        BORRADOR("Borrador"),
        VALIDADO("Validado"),
        CONTABILIZADO("Contabilizado"),
        ANULADO("Anulado");

        private final String descripcion;

        EstadoAsiento(String descripcion) {
            this.descripcion = descripcion;
        }

        public String getDescripcion() {
            return descripcion;
        }
    }
}