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
public class ResumenBancarioDTO {
    private LocalDate fecha;
    private String empresa;
    private List<CuentaBancariaDTO> cuentasBancarias;
    private BigDecimal totalSaldosLibros;
    private BigDecimal totalSaldosBanco;
    private BigDecimal totalDiferencias;
    private Integer cuentasConciliadas;
    private Integer cuentasPendientes;
    private List<MovimientoBancoDTO> movimientosRecientes;
}