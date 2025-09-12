package edu.gt.umg.mipymes.contable.mipymes_contable_api.service;

import edu.gt.umg.mipymes.contable.mipymes_contable_api.dto.*;
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
 * Service para generar Estados Financieros
 * - Balance General (Estado de Situación Financiera)
 * - Estado de Resultados (Estado de Pérdidas y Ganancias)
 */
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class EstadosFinancierosService {

    private final CuentaRepository cuentaRepository;

    /**
     * Generar Balance General a fecha actual
     */
    public BalanceGeneralDTO generarBalanceGeneral() {
        return generarBalanceGeneral(LocalDate.now());
    }

    /**
     * Generar Balance General a fecha específica
     */
    public BalanceGeneralDTO generarBalanceGeneral(LocalDate fechaCorte) {
        log.info("Generando Balance General al {}", fechaCorte);

        // Obtener todas las cuentas con saldo
        List<Cuenta> todasLasCuentas = cuentaRepository.obtenerCuentasConSaldo();

        // Clasificar cuentas por tipo
        List<LineaBalanceDTO> activosCorrientes = obtenerCuentasPorTipo(todasLasCuentas, Cuenta.TipoCuenta.ACTIVO);
        List<LineaBalanceDTO> pasivos = obtenerCuentasPorTipo(todasLasCuentas, Cuenta.TipoCuenta.PASIVO);
        List<LineaBalanceDTO> patrimonio = obtenerCuentasPorTipo(todasLasCuentas, Cuenta.TipoCuenta.PATRIMONIO);

        // Calcular totales
        BigDecimal totalActivos = calcularTotal(activosCorrientes);
        BigDecimal totalPasivos = calcularTotal(pasivos);
        BigDecimal totalPatrimonio = calcularTotal(patrimonio);

        // Crear Balance General
        BalanceGeneralDTO balance = new BalanceGeneralDTO();
        balance.setFechaCorte(fechaCorte);
        balance.setEmpresa("Almacén El Planeador - Propietario: Horacio Porras");
        balance.setPeriodo("Al " + fechaCorte.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));

        // Activos (por simplicidad, todos como corrientes para la MIPYME)
        balance.setActivosCorrientes(activosCorrientes);
        balance.setActivosNoCorrientes(List.of()); // Vacío por ahora
        balance.setTotalActivos(totalActivos);

        // Pasivos (todos como corrientes)
        balance.setPasivosCorrientes(pasivos);
        balance.setPasivosNoCorrientes(List.of()); // Vacío por ahora
        balance.setTotalPasivos(totalPasivos);

        // Patrimonio
        balance.setPatrimonio(patrimonio);
        balance.setTotalPatrimonio(totalPatrimonio);

        // Validar balance contable
        BigDecimal sumaPatrimonioMasPasivos = totalPasivos.add(totalPatrimonio);
        balance.setDiferencia(totalActivos.subtract(sumaPatrimonioMasPasivos));
        balance.setBalanceado(balance.getDiferencia().abs().compareTo(new BigDecimal("0.01")) < 0);

        log.info("Balance General generado - Activos: {}, Pasivos + Patrimonio: {}, Balanceado: {}", 
                totalActivos, sumaPatrimonioMasPasivos, balance.isBalanceado());

        return balance;
    }

    /**
     * Generar Estado de Resultados para un período
     */
    public EstadoResultadosDTO generarEstadoResultados(LocalDate fechaInicio, LocalDate fechaFin) {
        log.info("Generando Estado de Resultados del {} al {}", fechaInicio, fechaFin);

        // Obtener cuentas de resultados (ingresos y gastos)
        List<Cuenta> todasLasCuentas = cuentaRepository.obtenerCuentasConSaldo();

        List<LineaBalanceDTO> ingresos = obtenerCuentasPorTipo(todasLasCuentas, Cuenta.TipoCuenta.INGRESO);
        List<LineaBalanceDTO> gastos = obtenerCuentasPorTipo(todasLasCuentas, Cuenta.TipoCuenta.GASTO);

        // Separar Costo de Ventas de otros gastos (para MIPYME comercial)
        List<LineaBalanceDTO> costos = gastos.stream()
            .filter(linea -> linea.getCodigo().startsWith("5101")) // Costo de Ventas
            .collect(Collectors.toList());
        
        List<LineaBalanceDTO> gastosOperativos = gastos.stream()
            .filter(linea -> !linea.getCodigo().startsWith("5101")) // Otros gastos
            .collect(Collectors.toList());

        // Calcular totales
        BigDecimal totalIngresos = calcularTotal(ingresos);
        BigDecimal totalCostos = calcularTotal(costos);
        BigDecimal totalGastos = calcularTotal(gastosOperativos);

        // Calcular utilidades
        BigDecimal utilidadBruta = totalIngresos.subtract(totalCostos);
        BigDecimal utilidadNeta = utilidadBruta.subtract(totalGastos);

        // Crear Estado de Resultados
        EstadoResultadosDTO estado = new EstadoResultadosDTO();
        estado.setFechaInicio(fechaInicio);
        estado.setFechaFin(fechaFin);
        estado.setEmpresa("Almacén El Planeador - Propietario: Horacio Porras");
        estado.setPeriodo("Del " + fechaInicio.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) +
                         " al " + fechaFin.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));

        estado.setIngresos(ingresos);
        estado.setTotalIngresos(totalIngresos);
        
        estado.setCostos(costos);
        estado.setTotalCostos(totalCostos);
        
        estado.setUtilidadBruta(utilidadBruta);
        
        estado.setGastos(gastosOperativos);
        estado.setTotalGastos(totalGastos);
        
        estado.setUtilidadNeta(utilidadNeta);
        estado.setTipoResultado(utilidadNeta.compareTo(BigDecimal.ZERO) >= 0 ? "UTILIDAD" : "PÉRDIDA");

        log.info("Estado de Resultados generado - Ingresos: {}, Gastos: {}, Resultado: {} {}", 
                totalIngresos, totalGastos.add(totalCostos), estado.getTipoResultado(), utilidadNeta.abs());

        return estado;
    }

    /**
     * Estado de Resultados del año actual
     */
    public EstadoResultadosDTO generarEstadoResultadosAnual() {
        LocalDate inicioAno = LocalDate.now().withDayOfYear(1);
        LocalDate finAno = LocalDate.now();
        return generarEstadoResultados(inicioAno, finAno);
    }

    // ========== MÉTODOS AUXILIARES ==========

    /**
     * Obtener cuentas filtradas por tipo y convertirlas a LineaBalanceDTO
     */
    private List<LineaBalanceDTO> obtenerCuentasPorTipo(List<Cuenta> cuentas, Cuenta.TipoCuenta tipo) {
        return cuentas.stream()
            .filter(cuenta -> cuenta.getTipo() == tipo)
            .map(this::convertirCuentaALinea)
            .collect(Collectors.toList());
    }

    /**
     * Convertir Cuenta a LineaBalanceDTO
     */
    private LineaBalanceDTO convertirCuentaALinea(Cuenta cuenta) {
        return new LineaBalanceDTO(
            cuenta.getCodigo(),
            cuenta.getNombre(),
            cuenta.getTipo().getDescripcion(),
            cuenta.getNaturaleza().name(),
            cuenta.getSaldo().abs() // Usar valor absoluto para presentación
        );
    }

    /**
     * Calcular total de una lista de líneas
     */
    private BigDecimal calcularTotal(List<LineaBalanceDTO> lineas) {
        return lineas.stream()
            .map(linea -> linea.getSaldoDeudor().add(linea.getSaldoAcreedor()))
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}