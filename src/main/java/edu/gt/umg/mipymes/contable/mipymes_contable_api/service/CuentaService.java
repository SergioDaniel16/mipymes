package edu.gt.umg.mipymes.contable.mipymes_contable_api.service;

import edu.gt.umg.mipymes.contable.mipymes_contable_api.dto.CuentaDTO;
import edu.gt.umg.mipymes.contable.mipymes_contable_api.entity.Cuenta;
import edu.gt.umg.mipymes.contable.mipymes_contable_api.entity.Cuenta.TipoCuenta;
import edu.gt.umg.mipymes.contable.mipymes_contable_api.repository.CuentaRepository;
import edu.gt.umg.mipymes.contable.mipymes_contable_api.exception.CuentaNotFoundException;
import edu.gt.umg.mipymes.contable.mipymes_contable_api.exception.CodigoYaExisteException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service que contiene la lógica de negocio para el manejo de Cuentas
 * 
 * @Transactional: Asegura que las operaciones de BD sean consistentes
 * @RequiredArgsConstructor: Lombok genera constructor con campos final
 * @Slf4j: Lombok para logging
 */
@Service
@Transactional(readOnly = true)  // Por defecto las operaciones son solo lectura
@RequiredArgsConstructor  // Constructor automático para inyección de dependencias
@Slf4j  // Para logging
public class CuentaService {

    private final CuentaRepository cuentaRepository;

    /**
     * Obtener todas las cuentas activas
     */
    public List<CuentaDTO> obtenerTodasLasCuentas() {
        log.info("Obteniendo todas las cuentas activas");
        List<Cuenta> cuentas = cuentaRepository.findByActivaTrueOrderByCodigo();
        return convertirADTO(cuentas);
    }

    /**
     * Obtener cuenta por ID
     */
    public CuentaDTO obtenerCuentaPorId(Long id) {
        log.info("Buscando cuenta con ID: {}", id);
        Cuenta cuenta = cuentaRepository.findById(id)
            .orElseThrow(() -> new CuentaNotFoundException("Cuenta no encontrada con ID: " + id));
        return convertirADTO(cuenta);
    }

    /**
     * Obtener cuenta por código
     */
    public CuentaDTO obtenerCuentaPorCodigo(String codigo) {
        log.info("Buscando cuenta con código: {}", codigo);
        Cuenta cuenta = cuentaRepository.findByCodigo(codigo)
            .orElseThrow(() -> new CuentaNotFoundException("Cuenta no encontrada con código: " + codigo));
        return convertirADTO(cuenta);
    }

    /**
     * Obtener cuentas por tipo
     */
    public List<CuentaDTO> obtenerCuentasPorTipo(TipoCuenta tipo) {
        log.info("Obteniendo cuentas de tipo: {}", tipo);
        List<Cuenta> cuentas = cuentaRepository.findByTipoAndActivaTrue(tipo);
        return convertirADTO(cuentas);
    }

    /**
     * Buscar cuentas por nombre (búsqueda parcial)
     */
    public List<CuentaDTO> buscarCuentasPorNombre(String nombre) {
        log.info("Buscando cuentas que contengan: {}", nombre);
        List<Cuenta> cuentas = cuentaRepository.findByNombreContainingIgnoreCase(nombre);
        return convertirADTO(cuentas);
    }

    /**
     * Crear una nueva cuenta
     */
    @Transactional  // Esta operación modifica datos, necesita transacción de escritura
    public CuentaDTO crearCuenta(CuentaDTO cuentaDTO) {
        log.info("Creando nueva cuenta con código: {}", cuentaDTO.getCodigo());
        
        // Validar que el código no exista
        if (cuentaRepository.existsByCodigo(cuentaDTO.getCodigo())) {
            throw new CodigoYaExisteException("Ya existe una cuenta con código: " + cuentaDTO.getCodigo());
        }

        // Convertir DTO a entidad
        Cuenta cuenta = convertirAEntidad(cuentaDTO);
        
        // Guardar en base de datos
        Cuenta cuentaGuardada = cuentaRepository.save(cuenta);
        
        log.info("Cuenta creada exitosamente con ID: {}", cuentaGuardada.getId());
        return convertirADTO(cuentaGuardada);
    }

    /**
     * Actualizar una cuenta existente
     */
    @Transactional
    public CuentaDTO actualizarCuenta(Long id, CuentaDTO cuentaDTO) {
        log.info("Actualizando cuenta con ID: {}", id);
        
        Cuenta cuentaExistente = cuentaRepository.findById(id)
            .orElseThrow(() -> new CuentaNotFoundException("Cuenta no encontrada con ID: " + id));

        // Validar que el código no esté usado por otra cuenta
        if (!cuentaExistente.getCodigo().equals(cuentaDTO.getCodigo()) && 
            cuentaRepository.existsByCodigo(cuentaDTO.getCodigo())) {
            throw new CodigoYaExisteException("Ya existe una cuenta con código: " + cuentaDTO.getCodigo());
        }

        // Actualizar campos
        cuentaExistente.setCodigo(cuentaDTO.getCodigo());
        cuentaExistente.setNombre(cuentaDTO.getNombre());
        cuentaExistente.setTipo(cuentaDTO.getTipo());
        cuentaExistente.setNaturaleza(cuentaDTO.getNaturaleza());
        cuentaExistente.setDescripcion(cuentaDTO.getDescripcion());
        cuentaExistente.setActiva(cuentaDTO.getActiva());

        Cuenta cuentaActualizada = cuentaRepository.save(cuentaExistente);
        log.info("Cuenta actualizada exitosamente");
        return convertirADTO(cuentaActualizada);
    }

    /**
     * Desactivar una cuenta (no la eliminamos físicamente)
     */
    @Transactional
    public void desactivarCuenta(Long id) {
        log.info("Desactivando cuenta con ID: {}", id);
        
        Cuenta cuenta = cuentaRepository.findById(id)
            .orElseThrow(() -> new CuentaNotFoundException("Cuenta no encontrada con ID: " + id));
        
        cuenta.setActiva(false);
        cuentaRepository.save(cuenta);
        
        log.info("Cuenta desactivada exitosamente");
    }

    /**
     * Obtener el catálogo de cuentas (método específico del dominio contable)
     */
    public List<CuentaDTO> obtenerCatalogoCuentas() {
        log.info("Obteniendo catálogo completo de cuentas");
        List<Cuenta> cuentas = cuentaRepository.obtenerCatalogoActivo();
        return convertirADTO(cuentas);
    }

    // ========== MÉTODOS AUXILIARES ==========


    /**
     * Convertir lista de entidades a DTOs
     */
    private List<CuentaDTO> convertirADTO(List<Cuenta> cuentas) {
        return cuentas.stream()
            .map(this::convertirADTO)
            .collect(Collectors.toList());
    }

    /**
     * Convertir entidad a DTO
     */
    private CuentaDTO convertirADTO(Cuenta cuenta) {
        CuentaDTO dto = new CuentaDTO();
        dto.setId(cuenta.getId());
        dto.setCodigo(cuenta.getCodigo());
        dto.setNombre(cuenta.getNombre());
        dto.setTipo(cuenta.getTipo());
        dto.setNaturaleza(cuenta.getNaturaleza());
        dto.setSaldo(cuenta.getSaldo());
        dto.setActiva(cuenta.getActiva());
        dto.setDescripcion(cuenta.getDescripcion());
        dto.setFechaCreacion(cuenta.getFechaCreacion());
        dto.setFechaModificacion(cuenta.getFechaModificacion());
        return dto;
    }

    /**
     * Convertir DTO a entidad
     */
    private Cuenta convertirAEntidad(CuentaDTO dto) {
        Cuenta cuenta = new Cuenta();
        cuenta.setCodigo(dto.getCodigo());
        cuenta.setNombre(dto.getNombre());
        cuenta.setTipo(dto.getTipo());
        cuenta.setNaturaleza(dto.getNaturaleza());
        cuenta.setDescripcion(dto.getDescripcion());
        cuenta.setSaldo(dto.getSaldo() != null ? dto.getSaldo() : BigDecimal.ZERO);
        cuenta.setActiva(dto.getActiva() != null ? dto.getActiva() : true);
        return cuenta;
    }
}