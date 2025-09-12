package edu.gt.umg.mipymes.contable.mipymes_contable_api.repository;

import edu.gt.umg.mipymes.contable.mipymes_contable_api.entity.CuentaBancaria;
import edu.gt.umg.mipymes.contable.mipymes_contable_api.entity.CuentaBancaria.TipoCuentaBancaria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CuentaBancariaRepository extends JpaRepository<CuentaBancaria, Long> {

    /**
     * Buscar cuenta bancaria por número de cuenta
     */
    Optional<CuentaBancaria> findByNumeroCuenta(String numeroCuenta);

    /**
     * Verificar si existe una cuenta con el número
     */
    boolean existsByNumeroCuenta(String numeroCuenta);

    /**
     * Obtener cuentas bancarias activas
     */
    List<CuentaBancaria> findByActivaTrueOrderByNombre();

    /**
     * Obtener cuentas por banco
     */
    List<CuentaBancaria> findByBancoAndActivaTrueOrderByNombre(String banco);

    /**
     * Obtener cuentas por tipo
     */
    List<CuentaBancaria> findByTipoAndActivaTrueOrderByNombre(TipoCuentaBancaria tipo);

    /**
     * Obtener cuentas no conciliadas
     */
    @Query("SELECT cb FROM CuentaBancaria cb WHERE ABS(cb.saldoLibros - cb.saldoBanco) > 0.01 AND cb.activa = true ORDER BY cb.nombre")
    List<CuentaBancaria> obtenerCuentasNoConciliadas();

    /**
     * Obtener suma de saldos según libros
     */
    @Query("SELECT SUM(cb.saldoLibros) FROM CuentaBancaria cb WHERE cb.activa = true")
    java.math.BigDecimal obtenerTotalSaldosLibros();

    /**
     * Obtener suma de saldos según banco
     */
    @Query("SELECT SUM(cb.saldoBanco) FROM CuentaBancaria cb WHERE cb.activa = true")
    java.math.BigDecimal obtenerTotalSaldosBanco();
}