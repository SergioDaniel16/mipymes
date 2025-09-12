package edu.gt.umg.mipymes.contable.mipymes_contable_api.dto;

import edu.gt.umg.mipymes.contable.mipymes_contable_api.entity.Cliente.TipoCliente;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClienteDTO {
    private Long id;

    @NotBlank(message = "El código del cliente es obligatorio")
    private String codigo;

    @NotBlank(message = "El nombre del cliente es obligatorio")
    private String nombre;

    private String razonSocial;
    private String nit;
    private String dpi;
    private String telefono;

    @Email(message = "El formato del email no es válido")
    private String email;

    private String direccion;
    private TipoCliente tipoCliente;
    private BigDecimal limiteCredito;
    private BigDecimal saldoActual;
    private Integer diasCredito;
    private Boolean activo;
    private String observaciones;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaModificacion;

    // Campos calculados
    private BigDecimal saldoDisponible;
    private Boolean tieneCreditoDisponible;
    private String tipoClienteDescripcion;

    public void calcularCampos() {
        this.saldoDisponible = limiteCredito != null && saldoActual != null
            ? limiteCredito.subtract(saldoActual)
            : BigDecimal.ZERO;
            
        this.tieneCreditoDisponible = saldoDisponible.compareTo(BigDecimal.ZERO) > 0;
        
        this.tipoClienteDescripcion = tipoCliente != null
            ? tipoCliente.getDescripcion()
            : null;
    }
}