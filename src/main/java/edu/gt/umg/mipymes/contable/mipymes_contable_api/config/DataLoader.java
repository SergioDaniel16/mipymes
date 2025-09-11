package edu.gt.umg.mipymes.contable.mipymes_contable_api.config;

import edu.gt.umg.mipymes.contable.mipymes_contable_api.entity.Cuenta;
import edu.gt.umg.mipymes.contable.mipymes_contable_api.entity.Cuenta.TipoCuenta;
import edu.gt.umg.mipymes.contable.mipymes_contable_api.entity.Cuenta.NaturalezaCuenta;
import edu.gt.umg.mipymes.contable.mipymes_contable_api.repository.CuentaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * Clase que carga datos iniciales en la base de datos
 * Se ejecuta automáticamente al iniciar la aplicación
 * 
 * Basado en el caso "El Almacén El Planeador" del PDF
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class DataLoader implements CommandLineRunner {

    private final CuentaRepository cuentaRepository;

    @Override
    public void run(String... args) throws Exception {
        if (cuentaRepository.count() == 0) {
            log.info("Base de datos vacía. Cargando datos iniciales del Almacén El Planeador...");
            cargarCuentasIniciales();
            log.info("Datos iniciales cargados exitosamente");
        } else {
            log.info("Base de datos ya contiene datos. Omitiendo carga inicial.");
        }
    }

    private void cargarCuentasIniciales() {
        // ========== ACTIVOS ==========
        
        // Efectivo en Caja - Saldo inicial Q38,100
        crearCuenta("1001", "Efectivo en Caja", TipoCuenta.ACTIVO, NaturalezaCuenta.DEUDORA,
                   new BigDecimal("38100"), "Dinero en efectivo disponible en caja");

        // Cuenta Bancaria - Saldo inicial Q68,000
        crearCuenta("1002", "Bancos", TipoCuenta.ACTIVO, NaturalezaCuenta.DEUDORA,
                   new BigDecimal("68000"), "Cuenta corriente bancaria");

        // Mercaderías - Inventario inicial Q78,000
        crearCuenta("1101", "Inventario de Mercaderías", TipoCuenta.ACTIVO, NaturalezaCuenta.DEUDORA,
                   new BigDecimal("78000"), "Inventario inicial de mercaderías para venta");

        // Mobiliario y Equipo - Saldo inicial Q26,000
        crearCuenta("1201", "Mobiliario y Equipo", TipoCuenta.ACTIVO, NaturalezaCuenta.DEUDORA,
                   new BigDecimal("26000"), "Mobiliario y equipo de la empresa");

        // Clientes (para ventas a crédito)
        crearCuenta("1003", "Clientes", TipoCuenta.ACTIVO, NaturalezaCuenta.DEUDORA,
                   BigDecimal.ZERO, "Cuentas por cobrar a clientes");

        // Documentos por Cobrar (para ventas con documento)
        crearCuenta("1004", "Documentos por Cobrar", TipoCuenta.ACTIVO, NaturalezaCuenta.DEUDORA,
                   BigDecimal.ZERO, "Documentos por cobrar respaldados con pagarés");

        // ========== PASIVOS ==========
        
        // Proveedores - Saldo inicial Q37,000
        crearCuenta("2001", "Proveedores", TipoCuenta.PASIVO, NaturalezaCuenta.ACREEDORA,
                   new BigDecimal("37000"), "Cuentas por pagar a proveedores de mercaderías");

        // Letras de Cambio por Pagar - Saldo inicial Q8,000
        crearCuenta("2002", "Letras de Cambio por Pagar", TipoCuenta.PASIVO, NaturalezaCuenta.ACREEDORA,
                   new BigDecimal("8000"), "Letras de cambio por pagar (mobiliario y equipo)");

        // ========== PATRIMONIO ==========
        
        // Capital - Se calcula: Activos - Pasivos = 210,100 - 45,000 = 165,100
        crearCuenta("3001", "Capital", TipoCuenta.PATRIMONIO, NaturalezaCuenta.ACREEDORA,
                   new BigDecimal("165100"), "Capital del propietario Horacio Porras");

        // ========== INGRESOS ==========
        
        crearCuenta("4001", "Ventas", TipoCuenta.INGRESO, NaturalezaCuenta.ACREEDORA,
                   BigDecimal.ZERO, "Ingresos por ventas de mercaderías");

        // ========== GASTOS ==========
        
        crearCuenta("5001", "Alquiler", TipoCuenta.GASTO, NaturalezaCuenta.DEUDORA,
                   BigDecimal.ZERO, "Gasto por alquiler del local");

        crearCuenta("5002", "Publicidad", TipoCuenta.GASTO, NaturalezaCuenta.DEUDORA,
                   BigDecimal.ZERO, "Gastos de publicidad y marketing");

        crearCuenta("5003", "Sueldos", TipoCuenta.GASTO, NaturalezaCuenta.DEUDORA,
                   BigDecimal.ZERO, "Sueldos del personal");

        // ========== CUENTAS ADICIONALES PARA OPERACIONES ==========
        
        crearCuenta("5101", "Costo de Ventas", TipoCuenta.GASTO, NaturalezaCuenta.DEUDORA,
                   BigDecimal.ZERO, "Costo de las mercaderías vendidas");

        crearCuenta("1102", "Compras", TipoCuenta.ACTIVO, NaturalezaCuenta.DEUDORA,
                   BigDecimal.ZERO, "Compras de mercaderías");

        log.info("Total de cuentas creadas: {}", cuentaRepository.count());
    }

    private void crearCuenta(String codigo, String nombre, TipoCuenta tipo, 
                           NaturalezaCuenta naturaleza, BigDecimal saldo, String descripcion) {
        
        Cuenta cuenta = new Cuenta();
        cuenta.setCodigo(codigo);
        cuenta.setNombre(nombre);
        cuenta.setTipo(tipo);
        cuenta.setNaturaleza(naturaleza);
        cuenta.setSaldo(saldo);
        cuenta.setDescripcion(descripcion);
        cuenta.setActiva(true);
        
        cuentaRepository.save(cuenta);
        
        log.debug("Cuenta creada: {} - {} con saldo Q{}", codigo, nombre, saldo);
    }
}