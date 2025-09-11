package edu.gt.umg.mipymes.contable.mipymes_contable_api.service;

import edu.gt.umg.mipymes.contable.mipymes_contable_api.dto.BalanceComprobacionDTO;
import edu.gt.umg.mipymes.contable.mipymes_contable_api.dto.LineaBalanceDTO;
import edu.gt.umg.mipymes.contable.mipymes_contable_api.dto.TotalesBalanceDTO;
import edu.gt.umg.mipymes.contable.mipymes_contable_api.entity.Cuenta;
import edu.gt.umg.mipymes.contable.mipymes_contable_api.repository.CuentaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service para generar el Balance de Comprobación
 */
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class BalanceComprobacionService {

    private final CuentaRepository cuentaRepository;

    /**
     * Generar Balance de Comprobación a la fecha actual
     */
    public BalanceComprobacionDTO generarBalanceComprobacion() {
        return generarBalanceComprobacion(LocalDate.now());
    }

    /**
     * Generar Balance de Comprobación a una fecha específica
     */
    public BalanceComprobacionDTO generarBalanceComprobacion(LocalDate fechaCorte) {
        log.info("Generando Balance de Comprobación al {}", fechaCorte);

        // Obtener todas las cuentas activas con sus saldos
        List<Cuenta> cuentas = cuentaRepository.obtenerCatalogoActivo();

        // Convertir a líneas del balance
        List<LineaBalanceDTO> lineas = cuentas.stream()
            .map(this::convertirCuentaALineaBalance)
            .filter(linea -> !esSaldoCero(linea)) // Opcional: filtrar cuentas con saldo cero
            .collect(Collectors.toList());

        // Calcular totales
        TotalesBalanceDTO totales = calcularTotales(lineas);

        // Crear el balance completo
        BalanceComprobacionDTO balance = new BalanceComprobacionDTO();
        balance.setFechaCorte(fechaCorte);
        balance.setLineas(lineas);
        balance.setTotales(totales);
        balance.setBalanceado(esBalanceado(totales));
        balance.setEmpresa("Almacén El Planeador");
        balance.setPeriodo(formatearPeriodo(fechaCorte));

        log.info("Balance generado: {} cuentas, Balanceado: {}", 
                lineas.size(), balance.isBalanceado());

        return balance;
    }

    /**
     * Generar Balance de Comprobación solo con cuentas que tienen saldo
     */
    public BalanceComprobacionDTO generarBalanceConSaldo() {
        log.info("Generando Balance de Comprobación solo con cuentas con saldo");

        List<Cuenta> cuentasConSaldo = cuentaRepository.obtenerCuentasConSaldo();

        List<LineaBalanceDTO> lineas = cuentasConSaldo.stream()
            .map(this::convertirCuentaALineaBalance)
            .collect(Collectors.toList());

        TotalesBalanceDTO totales = calcularTotales(lineas);

        BalanceComprobacionDTO balance = new BalanceComprobacionDTO();
        balance.setFechaCorte(LocalDate.now());
        balance.setLineas(lineas);
        balance.setTotales(totales);
        balance.setBalanceado(esBalanceado(totales));
        balance.setEmpresa("Almacén El Planeador");
        balance.setPeriodo("Al " + LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));

        return balance;
    }

    /**
     * Generar Balance de Comprobación por tipo de cuenta
     */
    public BalanceComprobacionDTO generarBalancePorTipo(Cuenta.TipoCuenta tipo) {
        log.info("Generando Balance de Comprobación para cuentas de tipo: {}", tipo);

        List<Cuenta> cuentasPorTipo = cuentaRepository.findByTipoAndActivaTrue(tipo);

        List<LineaBalanceDTO> lineas = cuentasPorTipo.stream()
            .map(this::convertirCuentaALineaBalance)
            .collect(Collectors.toList());

        TotalesBalanceDTO totales = calcularTotales(lineas);

        BalanceComprobacionDTO balance = new BalanceComprobacionDTO();
        balance.setFechaCorte(LocalDate.now());
        balance.setLineas(lineas);
        balance.setTotales(totales);
        balance.setBalanceado(esBalanceado(totales));
        balance.setEmpresa("Almacén El Planeador");
        balance.setPeriodo("Cuentas de " + tipo.getDescripcion());

        return balance;
    }

    // ========== MÉTODOS AUXILIARES ==========

    /**
     * Convertir una cuenta a una línea del balance
     */
    private LineaBalanceDTO convertirCuentaALineaBalance(Cuenta cuenta) {
        return new LineaBalanceDTO(
            cuenta.getCodigo(),
            cuenta.getNombre(),
            cuenta.getTipo().getDescripcion(),
            cuenta.getNaturaleza().name(),
            cuenta.getSaldo()
        );
    }

    /**
     * Calcular los totales del balance
     */
    private TotalesBalanceDTO calcularTotales(List<LineaBalanceDTO> lineas) {
        BigDecimal totalDeudores = lineas.stream()
            .map(LineaBalanceDTO::getSaldoDeudor)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalAcreedores = lineas.stream()
            .map(LineaBalanceDTO::getSaldoAcreedor)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        int cuentasDeudoras = (int) lineas.stream()
            .filter(linea -> linea.getSaldoDeudor().compareTo(BigDecimal.ZERO) > 0)
            .count();

        int cuentasAcreedoras = (int) lineas.stream()
            .filter(linea -> linea.getSaldoAcreedor().compareTo(BigDecimal.ZERO) > 0)
            .count();

        TotalesBalanceDTO totales = new TotalesBalanceDTO(totalDeudores, totalAcreedores);
        totales.setTotalCuentas(lineas.size());
        totales.setCuentasDeudoras(cuentasDeudoras);
        totales.setCuentasAcreedoras(cuentasAcreedoras);

        return totales;
    }

    /**
     * Verificar si el balance está balanceado
     */
    private boolean esBalanceado(TotalesBalanceDTO totales) {
        return totales.getDiferencia().abs().compareTo(new BigDecimal("0.01")) < 0;
    }

    /**
     * Verificar si una línea tiene saldo cero
     */
    private boolean esSaldoCero(LineaBalanceDTO linea) {
        return linea.getSaldoDeudor().compareTo(BigDecimal.ZERO) == 0 && 
               linea.getSaldoAcreedor().compareTo(BigDecimal.ZERO) == 0;
    }

    /**
     * Formatear el período para el reporte
     */
    private String formatearPeriodo(LocalDate fecha) {
        return "Al " + fecha.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }
}