package edu.gt.umg.mipymes.contable.mipymes_contable_api.repository;

import edu.gt.umg.mipymes.contable.mipymes_contable_api.entity.MovimientoInventario;
import edu.gt.umg.mipymes.contable.mipymes_contable_api.entity.MovimientoInventario.TipoMovimiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MovimientoInventarioRepository extends JpaRepository<MovimientoInventario, Long> {

    /**
     * Obtener movimientos por producto
     */
    List<MovimientoInventario> findByProductoIdOrderByFechaMovimientoDesc(Long productoId);

    /**
     * Obtener movimientos por tipo
     */
    List<MovimientoInventario> findByTipoMovimientoOrderByFechaMovimientoDesc(TipoMovimiento tipoMovimiento);

    /**
     * Obtener movimientos por rango de fechas
     */
    List<MovimientoInventario> findByFechaMovimientoBetweenOrderByFechaMovimientoDesc(
        LocalDateTime fechaInicio, LocalDateTime fechaFin);

    /**
     * Obtener movimientos por producto en rango de fechas
     */
    @Query("SELECT m FROM MovimientoInventario m WHERE m.producto.id = :productoId " +
           "AND m.fechaMovimiento BETWEEN :fechaInicio AND :fechaFin ORDER BY m.fechaMovimiento DESC")
    List<MovimientoInventario> obtenerMovimientosPorProductoYFecha(
        @Param("productoId") Long productoId,
        @Param("fechaInicio") LocalDateTime fechaInicio,
        @Param("fechaFin") LocalDateTime fechaFin);

    /**
     * Obtener últimos movimientos (para dashboard)
     */
    @Query("SELECT m FROM MovimientoInventario m ORDER BY m.fechaMovimiento DESC LIMIT 10")
    List<MovimientoInventario> obtenerUltimosMovimientos();

    /**
     * Contar movimientos por tipo en un período
     */
    @Query("SELECT COUNT(m) FROM MovimientoInventario m WHERE m.tipoMovimiento = :tipo " +
           "AND m.fechaMovimiento BETWEEN :fechaInicio AND :fechaFin")
    Long contarMovimientosPorTipoYFecha(
        @Param("tipo") TipoMovimiento tipo,
        @Param("fechaInicio") LocalDateTime fechaInicio,
        @Param("fechaFin") LocalDateTime fechaFin);

    /**
     * Obtener valor total de movimientos por tipo
     */
    @Query("SELECT SUM(m.precioUnitario * m.cantidad) FROM MovimientoInventario m " +
           "WHERE m.tipoMovimiento = :tipo AND m.fechaMovimiento BETWEEN :fechaInicio AND :fechaFin")
    java.math.BigDecimal obtenerValorMovimientosPorTipo(
        @Param("tipo") TipoMovimiento tipo,
        @Param("fechaInicio") LocalDateTime fechaInicio,
        @Param("fechaFin") LocalDateTime fechaFin);
}