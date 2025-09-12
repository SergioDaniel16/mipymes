package edu.gt.umg.mipymes.contable.mipymes_contable_api.repository;

import edu.gt.umg.mipymes.contable.mipymes_contable_api.entity.Cliente;
import edu.gt.umg.mipymes.contable.mipymes_contable_api.entity.Cliente.TipoCliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    /**
     * Buscar cliente por código
     */
    Optional<Cliente> findByCodigo(String codigo);

    /**
     * Verificar si existe un cliente con el código
     */
    boolean existsByCodigo(String codigo);

    /**
     * Obtener clientes activos
     */
    List<Cliente> findByActivoTrueOrderByNombre();

    /**
     * Buscar clientes por nombre
     */
    List<Cliente> findByNombreContainingIgnoreCaseAndActivoTrue(String nombre);

    /**
     * Obtener clientes por tipo
     */
    List<Cliente> findByTipoClienteAndActivoTrueOrderByNombre(TipoCliente tipoCliente);

    /**
     * Obtener clientes con saldo pendiente
     */
    @Query("SELECT c FROM Cliente c WHERE c.saldoActual > 0 AND c.activo = true ORDER BY c.nombre")
    List<Cliente> obtenerClientesConSaldo();

    /**
     * Obtener clientes que exceden límite de crédito
     */
    @Query("SELECT c FROM Cliente c WHERE c.saldoActual > c.limiteCredito AND c.activo = true ORDER BY c.nombre")
    List<Cliente> obtenerClientesExcedenCredito();

    /**
     * Obtener total de cuentas por cobrar
     */
    @Query("SELECT SUM(c.saldoActual) FROM Cliente c WHERE c.activo = true")
    BigDecimal obtenerTotalCuentasPorCobrar();
}