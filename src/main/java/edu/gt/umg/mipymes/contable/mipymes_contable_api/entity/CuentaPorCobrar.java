package edu.gt.umg.mipymes.contable.mipymes_contable_api.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Entidad que representa una cuenta por cobrar (factura a crédito)
 */
@Entity
@Table(name = "cuentas_por_cobrar")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CuentaPorCobrar {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false)
    @NotNull(message = "El cliente es obligatorio")
    private Cliente cliente;

    @Column(nullable = false, unique = true, length = 50)
    @NotNull(message = "El número de documento es obligatorio")
    private String numeroDocumento;

    @Column(nullable = false)
    @NotNull(message = "La fecha de emisión es obligatoria")
    private LocalDate fechaEmision;

    @Column(nullable = false)
    @NotNull(message = "La fecha de vencimiento es obligatoria")
    private LocalDate fechaVencimiento;

    @Column(nullable = false, precision = 15, scale = 2)
    @NotNull(message = "El monto original es obligatorio")
    @Positive(message = "El monto debe ser positivo")
    private BigDecimal montoOriginal;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal montoAbonado = BigDecimal.ZERO;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal saldoPendiente;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoCuenta estado = EstadoCuenta.PENDIENTE;

    @Column(length = 500)
    private String descripcion;

    @Column(length = 500)
    private String observaciones;

    @Column(nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    @Column(nullable = false)
    private LocalDateTime fechaModificacion;

    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        fechaCreacion = now;
        fechaModificacion = now;
        if (saldoPendiente == null) {
            saldoPendiente = montoOriginal;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        fechaModificacion = LocalDateTime.now();
        saldoPendiente = montoOriginal.subtract(montoAbonado);
    }

    /**
     * Verificar si la cuenta está vencida
     */
    public boolean estaVencida() {
        return LocalDate.now().isAfter(fechaVencimiento) && estado == EstadoCuenta.PENDIENTE;
    }

    /**
     * Calcular días de atraso
     */
    public long getDiasAtraso() {
        if (estaVencida()) {
            return java.time.temporal.ChronoUnit.DAYS.between(fechaVencimiento, LocalDate.now());
        }
        return 0;
    }

    /**
     * Estados de la cuenta por cobrar
     */
    public enum EstadoCuenta {
        PENDIENTE("Pendiente"),
        PAGADO("Pagado"),
        VENCIDO("Vencido"),
        PARCIAL("Pago Parcial"),
        ANULADO("Anulado");

        private final String descripcion;

        EstadoCuenta(String descripcion) {
            this.descripcion = descripcion;
        }

        public String getDescripcion() {
            return descripcion;
        }
    }
}