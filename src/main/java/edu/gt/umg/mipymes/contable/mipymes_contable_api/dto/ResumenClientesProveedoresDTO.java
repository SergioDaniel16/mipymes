package edu.gt.umg.mipymes.contable.mipymes_contable_api.dto;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResumenClientesProveedoresDTO {
    private LocalDate fecha;
    private String empresa;
    
    // Resumen de clientes
    private Integer totalClientes;
    private Integer clientesActivos;
    private BigDecimal totalCuentasPorCobrar;
    private Integer cuentasVencidas;
    private List<ClienteDTO> clientesConMayorSaldo;
    
    // Resumen de proveedores
    private Integer totalProveedores;
    private Integer proveedoresActivos;
    private BigDecimal totalCuentasPorPagar;
    private Integer cuentasVencidasPagar;
    private List<ProveedorDTO> proveedoresConMayorSaldo;
    
    // Cuentas críticas
    private List<CuentaPorCobrarDTO> cuentasPorCobrarVencidas;
    private List<CuentaPorPagarDTO> cuentasPorPagarVencidas;
}