package edu.gt.umg.mipymes.contable.mipymes_contable_api.dto;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TotalesBalanceDTO {
    private BigDecimal totalDeudores = BigDecimal.ZERO;
    private BigDecimal totalAcreedores = BigDecimal.ZERO;
    private BigDecimal diferencia = BigDecimal.ZERO;
    private Integer totalCuentas = 0;
    private Integer cuentasDeudoras = 0;
    private Integer cuentasAcreedoras = 0;

    public TotalesBalanceDTO(BigDecimal totalDeudores, BigDecimal totalAcreedores) {
        this.totalDeudores = totalDeudores;
        this.totalAcreedores = totalAcreedores;
        this.diferencia = totalDeudores.subtract(totalAcreedores);
    }
}