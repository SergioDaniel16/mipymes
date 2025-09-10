package edu.gt.umg.mipymes.contable.mipymes_contable_api.dto;

import edu.gt.umg.mipymes.contable.mipymes_contable_api.entity.Cuenta.TipoCuenta;
import edu.gt.umg.mipymes.contable.mipymes_contable_api.entity.Cuenta.NaturalezaCuenta;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO para transferir datos de Cuenta entre las capas de la aplicación
 * Los DTOs nos permiten:
 * 1. Controlar qué datos exponemos en la API
 * 2. Validar datos de entrada
 * 3. Desacoplar la estructura interna (Entity) de la API externa
 */
@Data
public class CuentaDTO {

    private Long id;

    @NotBlank(message = "El código de la cuenta es obligatorio")
    @Pattern(regexp = "^[0-9]{4}$", message = "El código debe tener exactamente 4 dígitos")
    private String codigo;

    @NotBlank(message = "El nombre de la cuenta es obligatorio")
    private String nombre;

    @NotNull(message = "El tipo de cuenta es obligatorio")
    private TipoCuenta tipo;

    @NotNull(message = "La naturaleza de la cuenta es obligatoria")
    private NaturalezaCuenta naturaleza;

    private BigDecimal saldo;

    private Boolean activa;

    private String descripcion;

    private LocalDateTime fechaCreacion;

    private LocalDateTime fechaModificacion;

    // Campos adicionales calculados para la vista
    private String tipoDescripcion;
    private String naturalezaDescripcion;

    /**
     * Constructor que incluye las descripciones calculadas
     */
    public CuentaDTO(Long id, String codigo, String nombre, TipoCuenta tipo, 
                     NaturalezaCuenta naturaleza, BigDecimal saldo, Boolean activa, 
                     String descripcion, LocalDateTime fechaCreacion, LocalDateTime fechaModificacion) {
        this.id = id;
        this.codigo = codigo;
        this.nombre = nombre;
        this.tipo = tipo;
        this.naturaleza = naturaleza;
        this.saldo = saldo;
        this.activa = activa;
        this.descripcion = descripcion;
        this.fechaCreacion = fechaCreacion;
        this.fechaModificacion = fechaModificacion;
        
        // Calcular descripciones
        this.tipoDescripcion = tipo != null ? tipo.getDescripcion() : null;
        this.naturalezaDescripcion = naturaleza != null ? naturaleza.getDescripcion() : null;
    }

    public CuentaDTO() {}
}

/**
 * DTO simplificado para crear una nueva cuenta
 */
@Data
class CrearCuentaDTO {

    @NotBlank(message = "El código de la cuenta es obligatorio")
    @Pattern(regexp = "^[0-9]{4}$", message = "El código debe tener exactamente 4 dígitos")
    private String codigo;

    @NotBlank(message = "El nombre de la cuenta es obligatorio")
    private String nombre;

    @NotNull(message = "El tipo de cuenta es obligatorio")
    private TipoCuenta tipo;

    @NotNull(message = "La naturaleza de la cuenta es obligatoria")
    private NaturalezaCuenta naturaleza;

    private String descripcion;
}

/**
 * DTO para respuestas de resumen/reportes
 */
@Data
class ResumenCuentaDTO {
    private TipoCuenta tipo;
    private String tipoDescripcion;
    private Long totalCuentas;
    private BigDecimal saldoTotal;

    public ResumenCuentaDTO(TipoCuenta tipo, Long totalCuentas, BigDecimal saldoTotal) {
        this.tipo = tipo;
        this.tipoDescripcion = tipo.getDescripcion();
        this.totalCuentas = totalCuentas;
        this.saldoTotal = saldoTotal;
    }
}