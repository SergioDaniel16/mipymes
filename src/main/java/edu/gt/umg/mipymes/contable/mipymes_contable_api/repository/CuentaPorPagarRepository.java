package edu.gt.umg.mipymes.contable.mipymes_contable_api.repository;

import edu.gt.umg.mipymes.contable.mipymes_contable_api.entity.CuentaPorPagar;
import edu.gt.umg.mipymes.contable.mipymes_contable_api.entity.CuentaPorPagar.EstadoCuenta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface CuentaPorPagarRepository extends JpaRepository<CuentaPorPagar, Long> {

    /**
     * Obtener cuentas por pagar por proveedor
     */
    List<CuentaPorPagar> findByProveedorIdOrderByFechaVencimiento(Long proveedorId);

    /**
     * Obtener cuentas por pagar por estado
     */
    List<CuentaPorPagar> findByEstadoOrderByFechaVencimiento(EstadoCuenta estado);

    /**
     * Obtener cuentas por pagar vencidas
     */
    @Query("SELECT cxp FROM CuentaPorPagar cxp WHERE cxp.fechaVencimiento < :fecha AND cxp.estado = 'PENDIENTE' ORDER BY cxp.fechaVencimiento")
    List<CuentaPorPagar> obtenerCuentasVencidas(@Param("fecha") LocalDate fecha);

    /**
     * Obtener cuentas por pagar que vencen en un período
     */
    @Query("SELECT cxp FROM CuentaPorPagar cxp WHERE cxp.fechaVencimiento BETWEEN :fechaInicio AND :fechaFin ORDER BY cxp.fechaVencimiento")
    List<CuentaPorPagar> obtenerCuentasPorVencer(@Param("fechaInicio") LocalDate fechaInicio, @Param("fechaFin") LocalDate fechaFin);

    /**
     * Obtener total de cuentas por pagar pendientes
     */
    @Query("SELECT SUM(cxp.saldoPendiente) FROM CuentaPorPagar cxp WHERE cxp.estado IN ('PENDIENTE', 'PARCIAL')")
    java.math.BigDecimal obtenerTotalPendiente();
}
