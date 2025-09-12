package edu.gt.umg.mipymes.contable.mipymes_contable_api.repository;

import edu.gt.umg.mipymes.contable.mipymes_contable_api.entity.CuentaPorCobrar;
import edu.gt.umg.mipymes.contable.mipymes_contable_api.entity.CuentaPorCobrar.EstadoCuenta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface CuentaPorCobrarRepository extends JpaRepository<CuentaPorCobrar, Long> {

    /**
     * Obtener cuentas por cobrar por cliente
     */
    List<CuentaPorCobrar> findByClienteIdOrderByFechaVencimiento(Long clienteId);

    /**
     * Obtener cuentas por cobrar por estado
     */
    List<CuentaPorCobrar> findByEstadoOrderByFechaVencimiento(EstadoCuenta estado);

    /**
     * Obtener cuentas por cobrar vencidas
     */
    @Query("SELECT cxc FROM CuentaPorCobrar cxc WHERE cxc.fechaVencimiento < :fecha AND cxc.estado = 'PENDIENTE' ORDER BY cxc.fechaVencimiento")
    List<CuentaPorCobrar> obtenerCuentasVencidas(@Param("fecha") LocalDate fecha);

    /**
     * Obtener cuentas por cobrar que vencen en un período
     */
    @Query("SELECT cxc FROM CuentaPorCobrar cxc WHERE cxc.fechaVencimiento BETWEEN :fechaInicio AND :fechaFin ORDER BY cxc.fechaVencimiento")
    List<CuentaPorCobrar> obtenerCuentasPorVencer(@Param("fechaInicio") LocalDate fechaInicio, @Param("fechaFin") LocalDate fechaFin);

    /**
     * Obtener total de cuentas por cobrar pendientes
     */
    @Query("SELECT SUM(cxc.saldoPendiente) FROM CuentaPorCobrar cxc WHERE cxc.estado IN ('PENDIENTE', 'PARCIAL')")
    java.math.BigDecimal obtenerTotalPendiente();
}