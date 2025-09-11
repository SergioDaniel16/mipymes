package edu.gt.umg.mipymes.contable.mipymes_contable_api.service;

import edu.gt.umg.mipymes.contable.mipymes_contable_api.dto.AsientoContableDTO;
import edu.gt.umg.mipymes.contable.mipymes_contable_api.entity.*;
import edu.gt.umg.mipymes.contable.mipymes_contable_api.repository.AsientoContableRepository;
import edu.gt.umg.mipymes.contable.mipymes_contable_api.repository.CuentaRepository;
import edu.gt.umg.mipymes.contable.mipymes_contable_api.exception.AsientoNotFoundException;
import edu.gt.umg.mipymes.contable.mipymes_contable_api.exception.AsientoNoBalanceadoException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service para el manejo de Asientos Contables (Libro Diario)
 */
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class AsientoContableService {

    private final AsientoContableRepository asientoRepository;
    private final CuentaRepository cuentaRepository;

    /**
     * Obtener todos los asientos del libro diario
     */
    public List<AsientoContableDTO> obtenerLibroDiario() {
        log.info("Obteniendo libro diario completo");
        List<AsientoContable> asientos = asientoRepository.obtenerLibroDiario();
        return convertirADTO(asientos);
    }

    /**
     * Obtener asientos por período
     */
    public List<AsientoContableDTO> obtenerLibroDiarioPorPeriodo(LocalDate fechaInicio, LocalDate fechaFin) {
        log.info("Obteniendo libro diario del {} al {}", fechaInicio, fechaFin);
        List<AsientoContable> asientos = asientoRepository.obtenerLibroDiarioPorPeriodo(fechaInicio, fechaFin);
        return convertirADTO(asientos);
    }

    /**
     * Obtener asiento por ID
     */
    public AsientoContableDTO obtenerAsientoPorId(Long id) {
        log.info("Buscando asiento con ID: {}", id);
        AsientoContable asiento = asientoRepository.findById(id)
            .orElseThrow(() -> new AsientoNotFoundException("Asiento no encontrado con ID: " + id));
        return convertirADTO(asiento);
    }

    /**
     * Obtener asiento por número
     */
    public AsientoContableDTO obtenerAsientoPorNumero(Integer numero) {
        log.info("Buscando asiento número: {}", numero);
        AsientoContable asiento = asientoRepository.findByNumeroAsiento(numero)
            .orElseThrow(() -> new AsientoNotFoundException("Asiento no encontrado con número: " + numero));
        return convertirADTO(asiento);
    }

    /**
     * Crear un nuevo asiento contable
     */
    @Transactional
    public AsientoContableDTO crearAsiento(AsientoContableDTO asientoDTO) {
        log.info("Creando nuevo asiento contable");

        // Validar que el asiento esté balanceado
        if (!esAsientoBalanceado(asientoDTO)) {
            throw new AsientoNoBalanceadoException("El asiento no está balanceado. Débitos: " + 
                asientoDTO.getTotalDebitos() + ", Créditos: " + asientoDTO.getTotalCreditos());
        }

        // Obtener siguiente número de asiento
        if (asientoDTO.getNumeroAsiento() == null) {
            asientoDTO.setNumeroAsiento(asientoRepository.obtenerSiguienteNumeroAsiento());
        }

        // Convertir a entidad
        AsientoContable asiento = convertirAEntidad(asientoDTO);
        
        // Establecer estado inicial
        asiento.setEstado(AsientoContable.EstadoAsiento.BORRADOR);
        
        // Calcular totales
        asiento.calcularTotales();

        // Guardar
        AsientoContable asientoGuardado = asientoRepository.save(asiento);
        
        log.info("Asiento creado con número: {}", asientoGuardado.getNumeroAsiento());
        return convertirADTO(asientoGuardado);
    }

    /**
     * Contabilizar un asiento (cambiarlo a estado CONTABILIZADO y afectar saldos)
     */
    @Transactional
    public AsientoContableDTO contabilizarAsiento(Long id) {
        log.info("Contabilizando asiento con ID: {}", id);
        
        AsientoContable asiento = asientoRepository.findById(id)
            .orElseThrow(() -> new AsientoNotFoundException("Asiento no encontrado con ID: " + id));

        // Validar que esté balanceado
        if (!asiento.estaBalanceado()) {
            throw new AsientoNoBalanceadoException("No se puede contabilizar un asiento no balanceado");
        }

        // Cambiar estado
        asiento.setEstado(AsientoContable.EstadoAsiento.CONTABILIZADO);

        // Afectar saldos de las cuentas
        afectarSaldosCuentas(asiento);

        AsientoContable asientoActualizado = asientoRepository.save(asiento);
        log.info("Asiento {} contabilizado exitosamente", asiento.getNumeroAsiento());
        
        return convertirADTO(asientoActualizado);
    }

    /**
     * Buscar asientos por descripción
     */
    public List<AsientoContableDTO> buscarAsientosPorDescripcion(String descripcion) {
        log.info("Buscando asientos que contengan: {}", descripcion);
        List<AsientoContable> asientos = asientoRepository.findByDescripcionContainingIgnoreCaseOrderByNumeroAsiento(descripcion);
        return convertirADTO(asientos);
    }

    // ========== MÉTODOS AUXILIARES ==========

    /**
     * Validar si un asiento está balanceado
     */
    private boolean esAsientoBalanceado(AsientoContableDTO asiento) {
        return asiento.getTotalDebitos() != null && 
               asiento.getTotalCreditos() != null &&
               asiento.getTotalDebitos().compareTo(asiento.getTotalCreditos()) == 0;
    }

    /**
     * Afectar saldos de las cuentas cuando se contabiliza un asiento
     */
    private void afectarSaldosCuentas(AsientoContable asiento) {
        for (MovimientoContable movimiento : asiento.getMovimientos()) {
            Cuenta cuenta = movimiento.getCuenta();
            BigDecimal saldoActual = cuenta.getSaldo();
            
            // Calcular nuevo saldo según la naturaleza de la cuenta y tipo de movimiento
            BigDecimal nuevoSaldo;
            if (cuentaAumentaConDebito(cuenta)) {
                // Activos y Gastos aumentan con débitos
                nuevoSaldo = movimiento.esDebito() 
                    ? saldoActual.add(movimiento.getMonto())
                    : saldoActual.subtract(movimiento.getMonto());
            } else {
                // Pasivos, Patrimonio e Ingresos aumentan con créditos
                nuevoSaldo = movimiento.esCredito()
                    ? saldoActual.add(movimiento.getMonto())
                    : saldoActual.subtract(movimiento.getMonto());
            }
            
            cuenta.setSaldo(nuevoSaldo);
            cuentaRepository.save(cuenta);
            
            log.debug("Cuenta {} - Saldo anterior: {}, Nuevo saldo: {}", 
                cuenta.getCodigo(), saldoActual, nuevoSaldo);
        }
    }

    /**
     * Determinar si una cuenta aumenta con débitos
     */
    private boolean cuentaAumentaConDebito(Cuenta cuenta) {
        return cuenta.getTipo() == Cuenta.TipoCuenta.ACTIVO || 
               cuenta.getTipo() == Cuenta.TipoCuenta.GASTO;
    }

    /**
     * Convertir lista de entidades a DTOs
     */
    private List<AsientoContableDTO> convertirADTO(List<AsientoContable> asientos) {
        return asientos.stream()
            .map(this::convertirADTO)
            .collect(Collectors.toList());
    }

    /**
     * Convertir entidad a DTO
     */
    private AsientoContableDTO convertirADTO(AsientoContable asiento) {
        AsientoContableDTO dto = new AsientoContableDTO();
        dto.setId(asiento.getId());
        dto.setNumeroAsiento(asiento.getNumeroAsiento());
        dto.setFecha(asiento.getFecha());
        dto.setDescripcion(asiento.getDescripcion());
        dto.setReferencia(asiento.getReferencia());
        dto.setTipo(asiento.getTipo());
        dto.setTotalDebitos(asiento.getTotalDebitos());
        dto.setTotalCreditos(asiento.getTotalCreditos());
        dto.setEstado(asiento.getEstado());
        dto.setFechaCreacion(asiento.getFechaCreacion());
        dto.setFechaModificacion(asiento.getFechaModificacion());
        dto.setCreadoPor(asiento.getCreadoPor());
        
        // Calcular descripciones
        dto.calcularDescripciones();
        
        return dto;
    }

    /**
     * Convertir DTO a entidad
     */
    private AsientoContable convertirAEntidad(AsientoContableDTO dto) {
        AsientoContable asiento = new AsientoContable();
        asiento.setNumeroAsiento(dto.getNumeroAsiento());
        asiento.setFecha(dto.getFecha());
        asiento.setDescripcion(dto.getDescripcion());
        asiento.setReferencia(dto.getReferencia());
        asiento.setTipo(dto.getTipo());
        asiento.setTotalDebitos(dto.getTotalDebitos());
        asiento.setTotalCreditos(dto.getTotalCreditos());
        asiento.setCreadoPor(dto.getCreadoPor());
        
        return asiento;
    }
}