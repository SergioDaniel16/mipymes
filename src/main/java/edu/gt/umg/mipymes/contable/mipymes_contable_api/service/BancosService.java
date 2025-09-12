package edu.gt.umg.mipymes.contable.mipymes_contable_api.service;

import edu.gt.umg.mipymes.contable.mipymes_contable_api.dto.*;
import edu.gt.umg.mipymes.contable.mipymes_contable_api.entity.*;
import edu.gt.umg.mipymes.contable.mipymes_contable_api.repository.*;
import edu.gt.umg.mipymes.contable.mipymes_contable_api.exception.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service para el manejo de Bancos y Caja
 * - Gestión de cuentas bancarias
 * - Control de movimientos bancarios
 * - Conciliación bancaria
 */
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class BancosService {

    private final CuentaBancariaRepository cuentaBancariaRepository;
    private final MovimientoBancoRepository movimientoBancoRepository;

    // ========== GESTIÓN DE CUENTAS BANCARIAS ==========

    /**
     * Obtener todas las cuentas bancarias activas
     */
    public List<CuentaBancariaDTO> obtenerTodasLasCuentasBancarias() {
        log.info("Obteniendo todas las cuentas bancarias activas");
        List<CuentaBancaria> cuentas = cuentaBancariaRepository.findByActivaTrueOrderByNombre();
        return convertirCuentasADTO(cuentas);
    }

    /**
     * Obtener cuenta bancaria por ID
     */
    public CuentaBancariaDTO obtenerCuentaBancariaPorId(Long id) {
        log.info("Buscando cuenta bancaria con ID: {}", id);
        CuentaBancaria cuenta = cuentaBancariaRepository.findById(id)
            .orElseThrow(() -> new CuentaNotFoundException("Cuenta bancaria no encontrada con ID: " + id));
        return convertirCuentaADTO(cuenta);
    }

    /**
     * Crear una nueva cuenta bancaria
     */
    @Transactional
    public CuentaBancariaDTO crearCuentaBancaria(CuentaBancariaDTO cuentaDTO) {
        log.info("Creando nueva cuenta bancaria: {}", cuentaDTO.getNombre());

        // Validar que el número de cuenta no exista
        if (cuentaBancariaRepository.existsByNumeroCuenta(cuentaDTO.getNumeroCuenta())) {
            throw new CodigoYaExisteException("Ya existe una cuenta bancaria con número: " + cuentaDTO.getNumeroCuenta());
        }

        // Convertir a entidad y guardar
        CuentaBancaria cuenta = convertirDTOACuenta(cuentaDTO);
        CuentaBancaria cuentaGuardada = cuentaBancariaRepository.save(cuenta);

        log.info("Cuenta bancaria creada exitosamente con ID: {}", cuentaGuardada.getId());
        return convertirCuentaADTO(cuentaGuardada);
    }

    /**
     * Actualizar una cuenta bancaria existente
     */
    @Transactional
    public CuentaBancariaDTO actualizarCuentaBancaria(Long id, CuentaBancariaDTO cuentaDTO) {
        log.info("Actualizando cuenta bancaria con ID: {}", id);

        CuentaBancaria cuentaExistente = cuentaBancariaRepository.findById(id)
            .orElseThrow(() -> new CuentaNotFoundException("Cuenta bancaria no encontrada con ID: " + id));

        // Validar número de cuenta único
        if (!cuentaExistente.getNumeroCuenta().equals(cuentaDTO.getNumeroCuenta()) &&
            cuentaBancariaRepository.existsByNumeroCuenta(cuentaDTO.getNumeroCuenta())) {
            throw new CodigoYaExisteException("Ya existe una cuenta bancaria con número: " + cuentaDTO.getNumeroCuenta());
        }

        // Actualizar campos
        actualizarCamposCuenta(cuentaExistente, cuentaDTO);
        CuentaBancaria cuentaActualizada = cuentaBancariaRepository.save(cuentaExistente);

        log.info("Cuenta bancaria actualizada exitosamente");
        return convertirCuentaADTO(cuentaActualizada);
    }

    /**
     * Obtener cuentas no conciliadas
     */
    public List<CuentaBancariaDTO> obtenerCuentasNoConciliadas() {
        log.info("Obteniendo cuentas bancarias no conciliadas");
        List<CuentaBancaria> cuentas = cuentaBancariaRepository.obtenerCuentasNoConciliadas();
        return convertirCuentasADTO(cuentas);
    }

    // ========== MOVIMIENTOS BANCARIOS ==========

    /**
     * Registrar movimiento bancario
     */
    @Transactional
    public MovimientoBancoDTO registrarMovimientoBancario(CrearMovimientoBancoDTO movimientoDTO) {
        log.info("Registrando movimiento bancario para cuenta ID: {}", movimientoDTO.getCuentaBancariaId());

        // Obtener cuenta bancaria
        CuentaBancaria cuenta = cuentaBancariaRepository.findById(movimientoDTO.getCuentaBancariaId())
            .orElseThrow(() -> new CuentaNotFoundException("Cuenta bancaria no encontrada con ID: " + movimientoDTO.getCuentaBancariaId()));

        // Crear movimiento
        MovimientoBanco movimiento = new MovimientoBanco();
        movimiento.setCuentaBancaria(cuenta);
        movimiento.setTipoMovimiento(movimientoDTO.getTipoMovimiento());
        movimiento.setFechaMovimiento(movimientoDTO.getFechaMovimiento());
        movimiento.setMonto(movimientoDTO.getMonto());
        movimiento.setDescripcion(movimientoDTO.getDescripcion());
        movimiento.setNumeroDocumento(movimientoDTO.getNumeroDocumento());
        movimiento.setBeneficiario(movimientoDTO.getBeneficiario());
        movimiento.setObservaciones(movimientoDTO.getObservaciones());
        movimiento.setCreadoPor(movimientoDTO.getCreadoPor());

        // Actualizar saldo según libros de la cuenta
        BigDecimal nuevoSaldo;
        if (movimientoDTO.getTipoMovimiento().esCredito()) {
            nuevoSaldo = cuenta.getSaldoLibros().add(movimientoDTO.getMonto());
        } else {
            nuevoSaldo = cuenta.getSaldoLibros().subtract(movimientoDTO.getMonto());
        }

        cuenta.setSaldoLibros(nuevoSaldo);

        // Guardar movimiento y actualizar cuenta
        MovimientoBanco movimientoGuardado = movimientoBancoRepository.save(movimiento);
        cuentaBancariaRepository.save(cuenta);

        log.info("Movimiento bancario registrado exitosamente. Nuevo saldo: {}", nuevoSaldo);
        return convertirMovimientoADTO(movimientoGuardado);
    }

    /**
     * Obtener movimientos por cuenta bancaria
     */
    public List<MovimientoBancoDTO> obtenerMovimientosPorCuenta(Long cuentaId) {
        log.info("Obteniendo movimientos para cuenta bancaria ID: {}", cuentaId);
        List<MovimientoBanco> movimientos = movimientoBancoRepository.findByCuentaBancariaIdOrderByFechaMovimientoDesc(cuentaId);
        return convertirMovimientosADTO(movimientos);
    }

    /**
     * Obtener movimientos por rango de fechas
     */
    public List<MovimientoBancoDTO> obtenerMovimientosPorFecha(LocalDate fechaInicio, LocalDate fechaFin) {
        log.info("Obteniendo movimientos bancarios del {} al {}", fechaInicio, fechaFin);
        List<MovimientoBanco> movimientos = movimientoBancoRepository.findByFechaMovimientoBetweenOrderByFechaMovimientoDesc(fechaInicio, fechaFin);
        return convertirMovimientosADTO(movimientos);
    }

    /**
     * Obtener últimos movimientos bancarios
     */
    public List<MovimientoBancoDTO> obtenerUltimosMovimientos() {
        log.info("Obteniendo últimos movimientos bancarios");
        List<MovimientoBanco> movimientos = movimientoBancoRepository.obtenerUltimosMovimientos();
        return convertirMovimientosADTO(movimientos);
    }

    /**
     * Obtener cheques en circulación (pendientes)
     */
    public List<MovimientoBancoDTO> obtenerChequesEnCirculacion() {
        log.info("Obteniendo cheques en circulación");
        List<MovimientoBanco> cheques = movimientoBancoRepository.obtenerChequesEnCirculacion();
        return convertirMovimientosADTO(cheques);
    }

    /**
     * Conciliar movimiento bancario
     */
    @Transactional
    public MovimientoBancoDTO conciliarMovimiento(Long movimientoId, LocalDate fechaConciliacion) {
        log.info("Conciliando movimiento bancario ID: {}", movimientoId);

        MovimientoBanco movimiento = movimientoBancoRepository.findById(movimientoId)
            .orElseThrow(() -> new AsientoNotFoundException("Movimiento bancario no encontrado con ID: " + movimientoId));

        movimiento.setEstado(MovimientoBanco.EstadoMovimiento.CONCILIADO);
        movimiento.setFechaConciliacion(fechaConciliacion);

        MovimientoBanco movimientoActualizado = movimientoBancoRepository.save(movimiento);
        log.info("Movimiento bancario conciliado exitosamente");

        return convertirMovimientoADTO(movimientoActualizado);
    }

    // ========== REPORTES Y CONSULTAS ==========

    /**
     * Generar resumen bancario
     */
    public ResumenBancarioDTO generarResumenBancario() {
        log.info("Generando resumen bancario");

        List<CuentaBancaria> todasLasCuentas = cuentaBancariaRepository.findByActivaTrueOrderByNombre();
        List<MovimientoBanco> movimientosRecientes = movimientoBancoRepository.obtenerUltimosMovimientos();

        BigDecimal totalSaldosLibros = cuentaBancariaRepository.obtenerTotalSaldosLibros();
        BigDecimal totalSaldosBanco = cuentaBancariaRepository.obtenerTotalSaldosBanco();

        long cuentasConciliadas = todasLasCuentas.stream()
            .mapToLong(cuenta -> cuenta.estaConciliada() ? 1 : 0)
            .sum();

        ResumenBancarioDTO resumen = new ResumenBancarioDTO();
        resumen.setFecha(LocalDate.now());
        resumen.setEmpresa("Almacén El Planeador - Horacio Porras");
        resumen.setCuentasBancarias(convertirCuentasADTO(todasLasCuentas));
        resumen.setTotalSaldosLibros(totalSaldosLibros != null ? totalSaldosLibros : BigDecimal.ZERO);
        resumen.setTotalSaldosBanco(totalSaldosBanco != null ? totalSaldosBanco : BigDecimal.ZERO);
        resumen.setTotalDiferencias(resumen.getTotalSaldosLibros().subtract(resumen.getTotalSaldosBanco()));
        resumen.setCuentasConciliadas((int) cuentasConciliadas);
        resumen.setCuentasPendientes(todasLasCuentas.size() - (int) cuentasConciliadas);
        resumen.setMovimientosRecientes(convertirMovimientosADTO(movimientosRecientes));

        return resumen;
    }

    /**
     * Actualizar saldo bancario (para conciliación manual)
     */
    @Transactional
    public CuentaBancariaDTO actualizarSaldoBanco(Long cuentaId, BigDecimal nuevoSaldoBanco) {
        log.info("Actualizando saldo bancario para cuenta ID: {} - Nuevo saldo: {}", cuentaId, nuevoSaldoBanco);

        CuentaBancaria cuenta = cuentaBancariaRepository.findById(cuentaId)
            .orElseThrow(() -> new CuentaNotFoundException("Cuenta bancaria no encontrada con ID: " + cuentaId));

        cuenta.setSaldoBanco(nuevoSaldoBanco);
        CuentaBancaria cuentaActualizada = cuentaBancariaRepository.save(cuenta);

        log.info("Saldo bancario actualizado exitosamente");
        return convertirCuentaADTO(cuentaActualizada);
    }

    // ========== MÉTODOS AUXILIARES ==========

    private List<CuentaBancariaDTO> convertirCuentasADTO(List<CuentaBancaria> cuentas) {
        return cuentas.stream()
            .map(this::convertirCuentaADTO)
            .collect(Collectors.toList());
    }

    private CuentaBancariaDTO convertirCuentaADTO(CuentaBancaria cuenta) {
        CuentaBancariaDTO dto = new CuentaBancariaDTO();
        dto.setId(cuenta.getId());
        dto.setNombre(cuenta.getNombre());
        dto.setBanco(cuenta.getBanco());
        dto.setNumeroCuenta(cuenta.getNumeroCuenta());
        dto.setTipo(cuenta.getTipo());
        dto.setSaldoLibros(cuenta.getSaldoLibros());
        dto.setSaldoBanco(cuenta.getSaldoBanco());
        dto.setActiva(cuenta.getActiva());
        dto.setDescripcion(cuenta.getDescripcion());
        dto.setFechaCreacion(cuenta.getFechaCreacion());
        dto.setFechaModificacion(cuenta.getFechaModificacion());

        dto.calcularCampos(); // Calcular campos derivados
        return dto;
    }

    private CuentaBancaria convertirDTOACuenta(CuentaBancariaDTO dto) {
        CuentaBancaria cuenta = new CuentaBancaria();
        cuenta.setNombre(dto.getNombre());
        cuenta.setBanco(dto.getBanco());
        cuenta.setNumeroCuenta(dto.getNumeroCuenta());
        cuenta.setTipo(dto.getTipo());
        cuenta.setSaldoLibros(dto.getSaldoLibros() != null ? dto.getSaldoLibros() : BigDecimal.ZERO);
        cuenta.setSaldoBanco(dto.getSaldoBanco() != null ? dto.getSaldoBanco() : BigDecimal.ZERO);
        cuenta.setActiva(dto.getActiva() != null ? dto.getActiva() : true);
        cuenta.setDescripcion(dto.getDescripcion());
        return cuenta;
    }

    private void actualizarCamposCuenta(CuentaBancaria cuenta, CuentaBancariaDTO dto) {
        cuenta.setNombre(dto.getNombre());
        cuenta.setBanco(dto.getBanco());
        cuenta.setNumeroCuenta(dto.getNumeroCuenta());
        cuenta.setTipo(dto.getTipo());
        cuenta.setActiva(dto.getActiva());
        cuenta.setDescripcion(dto.getDescripcion());
        // No actualizamos saldos aquí, se hace a través de movimientos
    }

    private List<MovimientoBancoDTO> convertirMovimientosADTO(List<MovimientoBanco> movimientos) {
        return movimientos.stream()
            .map(this::convertirMovimientoADTO)
            .collect(Collectors.toList());
    }

    private MovimientoBancoDTO convertirMovimientoADTO(MovimientoBanco movimiento) {
        MovimientoBancoDTO dto = new MovimientoBancoDTO();
        dto.setId(movimiento.getId());
        dto.setCuentaBancariaId(movimiento.getCuentaBancaria().getId());
        dto.setCuentaBancariaNombre(movimiento.getCuentaBancaria().getNombre());
        dto.setTipoMovimiento(movimiento.getTipoMovimiento());
        dto.setFechaMovimiento(movimiento.getFechaMovimiento());
        dto.setMonto(movimiento.getMonto());
        dto.setDescripcion(movimiento.getDescripcion());
        dto.setNumeroDocumento(movimiento.getNumeroDocumento());
        dto.setBeneficiario(movimiento.getBeneficiario());
        dto.setEstado(movimiento.getEstado());
        dto.setFechaConciliacion(movimiento.getFechaConciliacion());
        dto.setObservaciones(movimiento.getObservaciones());
        dto.setFechaCreacion(movimiento.getFechaCreacion());
        dto.setFechaModificacion(movimiento.getFechaModificacion());
        dto.setCreadoPor(movimiento.getCreadoPor());

        dto.calcularCampos(); // Calcular campos derivados
        return dto;
    }
}