package edu.gt.umg.mipymes.contable.mipymes_contable_api.dto;

import edu.gt.umg.mipymes.contable.mipymes_contable_api.entity.AsientoContable.TipoAsiento;
import edu.gt.umg.mipymes.contable.mipymes_contable_api.entity.AsientoContable.EstadoAsiento;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.Valid;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;

/**
 * DTO para transferir datos de AsientoContable
 */
@Data
public class AsientoContableDTO {

    private Long id;

    private Integer numeroAsiento;

    @NotNull(message = "La fecha del asiento es obligatoria")
    private LocalDate fecha;

    @NotBlank(message = "La descripción del asiento es obligatoria")
    private String descripcion;

    private String referencia;

    @NotNull(message = "El tipo de asiento es obligatorio")
    private TipoAsiento tipo;

    private BigDecimal totalDebitos;

    private BigDecimal totalCreditos;

    private EstadoAsiento estado;

    @Valid
    private List<MovimientoContableDTO> movimientos = new ArrayList<>();

    private LocalDateTime fechaCreacion;

    private LocalDateTime fechaModificacion;

    private String creadoPor;

    // Campos calculados para la vista
    private String tipoDescripcion;
    private String estadoDescripcion;
    private boolean balanceado;

    public AsientoContableDTO() {}

    /**
     * Método para calcular las descripciones
     */
    public void calcularDescripciones() {
        this.tipoDescripcion = tipo != null ? tipo.getDescripcion() : null;
        this.estadoDescripcion = estado != null ? estado.getDescripcion() : null;
        this.balanceado = totalDebitos != null && totalCreditos != null && 
                         totalDebitos.compareTo(totalCreditos) == 0;
    }
}

/**
 * DTO para MovimientoContable
 */
@Data
class MovimientoContableDTO {

    private Long id;

    @NotNull(message = "La cuenta es obligatoria")
    private Long cuentaId;

    private String cuentaCodigo;
    private String cuentaNombre;

    @NotNull(message = "El tipo de movimiento es obligatorio")
    private edu.gt.umg.mipymes.contable.mipymes_contable_api.entity.MovimientoContable.TipoMovimiento tipoMovimiento;

    @NotNull(message = "El monto es obligatorio")
    private BigDecimal monto;

    private String descripcion;

    private Integer orden;

    private LocalDateTime fechaCreacion;

    private LocalDateTime fechaModificacion;

    // Campos calculados
    private String tipoMovimientoDescripcion;

    public MovimientoContableDTO() {}

    public void calcularDescripciones() {
        this.tipoMovimientoDescripcion = tipoMovimiento != null ? tipoMovimiento.getDescripcion() : null;
    }
}

/**
 * DTO simplificado para crear asientos
 */
@Data
class CrearAsientoDTO {

    @NotNull(message = "La fecha del asiento es obligatoria")
    private LocalDate fecha;

    @NotBlank(message = "La descripción del asiento es obligatoria")
    private String descripcion;

    private String referencia;

    @NotNull(message = "El tipo de asiento es obligatorio")
    private TipoAsiento tipo;

    @Valid
    @NotNull(message = "Los movimientos son obligatorios")
    private List<CrearMovimientoDTO> movimientos = new ArrayList<>();

    private String creadoPor;
}

/**
 * DTO simplificado para crear movimientos
 */
@Data
class CrearMovimientoDTO {

    @NotNull(message = "La cuenta es obligatoria")
    private Long cuentaId;

    @NotNull(message = "El tipo de movimiento es obligatorio")
    private edu.gt.umg.mipymes.contable.mipymes_contable_api.entity.MovimientoContable.TipoMovimiento tipoMovimiento;

    @NotNull(message = "El monto es obligatorio")
    private BigDecimal monto;

    private String descripcion;

    private Integer orden = 1;
}