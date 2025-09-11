package edu.gt.umg.mipymes.contable.mipymes_contable_api.dto;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LineaBalanceDTO {
    private String codigo;
    private String nombreCuenta;
    private String tipoCuenta;
    private String naturaleza;
    private BigDecimal saldoDeudor = BigDecimal.ZERO;
    private BigDecimal saldoAcreedor = BigDecimal.ZERO;

    public LineaBalanceDTO(String codigo, String nombreCuenta, String tipoCuenta, 
                          String naturaleza, BigDecimal saldo) {
        this.codigo = codigo;
        this.nombreCuenta = nombreCuenta;
        this.tipoCuenta = tipoCuenta;
        this.naturaleza = naturaleza;
        
        if (naturaleza.equals("DEUDORA")) {
            this.saldoDeudor = saldo.compareTo(BigDecimal.ZERO) >= 0 ? saldo : BigDecimal.ZERO;
            this.saldoAcreedor = saldo.compareTo(BigDecimal.ZERO) < 0 ? saldo.abs() : BigDecimal.ZERO;
        } else {
            this.saldoAcreedor = saldo.compareTo(BigDecimal.ZERO) >= 0 ? saldo : BigDecimal.ZERO;
            this.saldoDeudor = saldo.compareTo(BigDecimal.ZERO) < 0 ? saldo.abs() : BigDecimal.ZERO;
        }
    }
}