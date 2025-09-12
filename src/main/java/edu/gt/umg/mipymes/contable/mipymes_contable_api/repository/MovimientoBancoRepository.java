package edu.gt.umg.mipymes.contable.mipymes_contable_api.repository;

import edu.gt.umg.mipymes.contable.mipymes_contable_api.entity.MovimientoBanco;
import edu.gt.umg.mipymes.contable.mipymes_contable_api.entity.MovimientoBanco.TipoMovimientoBanco;
import edu.gt.umg.mipymes.contable.mipymes_contable_api.entity.MovimientoBanco.EstadoMovimiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface MovimientoBancoRepository extends JpaRepository<MovimientoBanco, Long> {

    /**
     * Obtener movimientos por cuenta bancaria
     */
    List<MovimientoBanco> findByCuentaBancariaIdOrderByFechaMovimientoDesc(Long cuentaBancariaId);

    /**
     * Obtener movimientos por rango de fechas
     */
    List<MovimientoBanco> findByFechaMovimientoBetweenOrderByFechaMovimientoDesc(
        LocalDate fechaInicio, LocalDate fechaFin);

    /**
     * Obtener movimientos por cuenta y rango de fechas
     */
    @Query("SELECT mb FROM MovimientoBanco mb WHERE mb.cuentaBancaria.id = :cuentaId " +
           "AND mb.fechaMovimiento BETWEEN :fechaInicio AND :fechaFin ORDER BY mb.fechaMovimiento DESC")
    List<MovimientoBanco> obtenerMovimientosPorCuentaYFecha(
        @Param("cuentaId") Long cuentaId,
        @Param("fechaInicio") LocalDate fechaInicio,
        @Param("fechaFin") LocalDate fechaFin);

    /**
     * Obtener movimientos pendientes de conciliación
     */
    List<MovimientoBanco> findByEstadoOrderByFechaMovimientoDesc(EstadoMovimiento estado);

    /**
     * Obtener cheques emitidos pendientes
     */
    @Query("SELECT mb FROM MovimientoBanco mb WHERE mb.tipoMovimiento = 'CHEQUE_EMITIDO' " +
           "AND mb.estado = 'PENDIENTE' ORDER BY mb.fechaMovimiento DESC")
    List<MovimientoBanco> obtenerChequesEnCirculacion();

    /**
     * Obtener últimos movimientos
     */
    @Query("SELECT mb FROM MovimientoBanco mb ORDER BY mb.fechaCreacion DESC LIMIT 10")
    List<MovimientoBanco> obtenerUltimosMovimientos();
}