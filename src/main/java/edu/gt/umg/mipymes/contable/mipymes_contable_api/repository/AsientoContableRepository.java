package edu.gt.umg.mipymes.contable.mipymes_contable_api.repository;

import edu.gt.umg.mipymes.contable.mipymes_contable_api.entity.AsientoContable;
import edu.gt.umg.mipymes.contable.mipymes_contable_api.entity.AsientoContable.TipoAsiento;
import edu.gt.umg.mipymes.contable.mipymes_contable_api.entity.AsientoContable.EstadoAsiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Repository para operaciones de base de datos de AsientoContable
 */
@Repository
public interface AsientoContableRepository extends JpaRepository<AsientoContable, Long> {

    /**
     * Buscar asiento por número
     */
    Optional<AsientoContable> findByNumeroAsiento(Integer numeroAsiento);

    /**
     * Verificar si existe un número de asiento
     */
    boolean existsByNumeroAsiento(Integer numeroAsiento);

    /**
     * Obtener el siguiente número de asiento disponible
     */
    @Query("SELECT COALESCE(MAX(a.numeroAsiento), 0) + 1 FROM AsientoContable a")
    Integer obtenerSiguienteNumeroAsiento();

    /**
     * Buscar asientos por fecha
     */
    List<AsientoContable> findByFechaBetweenOrderByNumeroAsiento(LocalDate fechaInicio, LocalDate fechaFin);

    /**
     * Buscar asientos por tipo
     */
    List<AsientoContable> findByTipoOrderByNumeroAsiento(TipoAsiento tipo);

    /**
     * Buscar asientos por estado
     */
    List<AsientoContable> findByEstadoOrderByNumeroAsiento(EstadoAsiento estado);

    /**
     * Buscar asientos por descripción (búsqueda parcial)
     */
    List<AsientoContable> findByDescripcionContainingIgnoreCaseOrderByNumeroAsiento(String descripcion);

    /**
     * Obtener libro diario completo ordenado por número de asiento
     */
    @Query("SELECT a FROM AsientoContable a WHERE a.estado != 'ANULADO' ORDER BY a.numeroAsiento")
    List<AsientoContable> obtenerLibroDiario();

    /**
     * Obtener libro diario por período
     */
    @Query("SELECT a FROM AsientoContable a WHERE a.estado != 'ANULADO' AND a.fecha BETWEEN :fechaInicio AND :fechaFin ORDER BY a.numeroAsiento")
    List<AsientoContable> obtenerLibroDiarioPorPeriodo(@Param("fechaInicio") LocalDate fechaInicio, 
                                                       @Param("fechaFin") LocalDate fechaFin);

    /**
     * Obtener asientos que afectan una cuenta específica
     */
    @Query("SELECT DISTINCT a FROM AsientoContable a JOIN a.movimientos m WHERE m.cuenta.id = :cuentaId AND a.estado = 'CONTABILIZADO' ORDER BY a.numeroAsiento")
    List<AsientoContable> obtenerAsientosPorCuenta(@Param("cuentaId") Long cuentaId);

    /**
     * Obtener asientos no balanceados (para validación)
     */
    @Query("SELECT a FROM AsientoContable a WHERE a.totalDebitos != a.totalCreditos ORDER BY a.numeroAsiento")
    List<AsientoContable> obtenerAsientosNoBalanceados();
}