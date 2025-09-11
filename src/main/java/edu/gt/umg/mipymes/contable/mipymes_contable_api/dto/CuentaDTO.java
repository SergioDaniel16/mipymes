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

    // Constructor vacío (Lombok lo genera automáticamente con @Data)
    public CuentaDTO() {}
}