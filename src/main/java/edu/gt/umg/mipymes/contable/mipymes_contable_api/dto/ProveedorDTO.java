package edu.gt.umg.mipymes.contable.mipymes_contable_api.dto;

import edu.gt.umg.mipymes.contable.mipymes_contable_api.entity.Proveedor.TipoProveedor;
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
public class ProveedorDTO {
    private Long id;

    @NotBlank(message = "El código del proveedor es obligatorio")
    private String codigo;

    @NotBlank(message = "El nombre del proveedor es obligatorio")
    private String nombre;

    private String razonSocial;
    private String nit;
    private String telefono;

    @Email(message = "El formato del email no es válido")
    private String email;

    private String direccion;
    private String contacto;
    private TipoProveedor tipoProveedor;
    private BigDecimal saldoActual;
    private Integer diasPago;
    private Boolean activo;
    private String observaciones;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaModificacion;

    // Campos calculados
    private Boolean tieneSaldoPendiente;
    private String tipoProveedorDescripcion;

    public void calcularCampos() {
        this.tieneSaldoPendiente = saldoActual != null && saldoActual.compareTo(BigDecimal.ZERO) > 0;
        
        this.tipoProveedorDescripcion = tipoProveedor != null
            ? tipoProveedor.getDescripcion()
            : null;
    }
}