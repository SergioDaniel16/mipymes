package edu.gt.umg.mipymes.contable.mipymes_contable_api.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entidad que representa una cuenta contable en el Catálogo de Cuentas
 * Ejemplo: "Efectivo en Caja", "Bancos", "Proveedores", etc.
 */
@Entity
@Table(name = "cuentas")
@Data  // Lombok: genera getters, setters, toString, equals, hashCode
@NoArgsConstructor  // Constructor sin argumentos (requerido por JPA)
@AllArgsConstructor // Constructor con todos los argumentos
public class Cuenta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Código único de la cuenta (ej: "1001", "2001", "5001")
     * En contabilidad se usan códigos numéricos para organizar las cuentas
     */
    @Column(unique = true, nullable = false, length = 10)
    @NotBlank(message = "El código de la cuenta es obligatorio")
    private String codigo;

    /**
     * Nombre descriptivo de la cuenta
     * Ejemplo: "Efectivo en Caja", "Cuentas por Pagar Proveedores"
     */
    @Column(nullable = false, length = 100)
    @NotBlank(message = "El nombre de la cuenta es obligatorio")
    private String nombre;

    /**
     * Tipo de cuenta según la ecuación contable:
     * ACTIVO, PASIVO, PATRIMONIO, INGRESO, GASTO
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NotNull(message = "El tipo de cuenta es obligatorio")
    private TipoCuenta tipo;

    /**
     * Naturaleza de la cuenta: DEUDORA o ACREEDORA
     * - DEUDORA: Activos y Gastos (aumentan con débitos)
     * - ACREEDORA: Pasivos, Patrimonio e Ingresos (aumentan con créditos)
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NotNull(message = "La naturaleza de la cuenta es obligatoria")
    private NaturalezaCuenta naturaleza;

    /**
     * Saldo actual de la cuenta
     * Usamos BigDecimal para precisión en cálculos monetarios
     */
    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal saldo = BigDecimal.ZERO;

    /**
     * Indica si la cuenta está activa (puede usarse en transacciones)
     */
    @Column(nullable = false)
    private Boolean activa = true;

    /**
     * Descripción adicional de la cuenta (opcional)
     */
    @Column(length = 500)
    private String descripcion;

    /**
     * Auditoría: cuándo se creó la cuenta
     */
    @Column(nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    /**
     * Auditoría: última modificación
     */
    @Column(nullable = false)
    private LocalDateTime fechaModificacion;

    /**
     * Método que se ejecuta antes de persistir la entidad
     */
    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        fechaCreacion = now;
        fechaModificacion = now;
    }

    /**
     * Método que se ejecuta antes de actualizar la entidad
     */
    @PreUpdate
    protected void onUpdate() {
        fechaModificacion = LocalDateTime.now();
    }

    /**
     * Enum para tipos de cuenta según la ecuación contable
     */
    public enum TipoCuenta {
        ACTIVO("Activo"),
        PASIVO("Pasivo"), 
        PATRIMONIO("Patrimonio"),
        INGRESO("Ingreso"),
        GASTO("Gasto");

        private final String descripcion;

        TipoCuenta(String descripcion) {
            this.descripcion = descripcion;
        }

        public String getDescripcion() {
            return descripcion;
        }
    }

    /**
     * Enum para la naturaleza contable de las cuentas
     */
    public enum NaturalezaCuenta {
        DEUDORA("Deudora - Aumenta con Débitos"),
        ACREEDORA("Acreedora - Aumenta con Créditos");

        private final String descripcion;

        NaturalezaCuenta(String descripcion) {
            this.descripcion = descripcion;
        }

        public String getDescripcion() {
            return descripcion;
        }
    }
}