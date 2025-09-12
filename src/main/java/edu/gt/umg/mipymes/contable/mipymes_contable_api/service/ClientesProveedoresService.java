package edu.gt.umg.mipymes.contable.mipymes_contable_api.service;

import edu.gt.umg.mipymes.contable.mipymes_contable_api.dto.*;
import edu.gt.umg.mipymes.contable.mipymes_contable_api.entity.*;
import edu.gt.umg.mipymes.contable.mipymes_contable_api.repository.*;
import edu.gt.umg.mipymes.contable.mipymes_contable_api.exception.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service para el manejo de Clientes y Proveedores
 * - Gestión de clientes y proveedores
 * - Control de cuentas por cobrar y pagar
 * - Reportes de cartera
 */
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class ClientesProveedoresService {

    private final ClienteRepository clienteRepository;
    private final ProveedorRepository proveedorRepository;
    private final CuentaPorCobrarRepository cuentaPorCobrarRepository;
    private final CuentaPorPagarRepository cuentaPorPagarRepository;

    // ========== GESTIÓN DE CLIENTES ==========

    /**
     * Obtener todos los clientes activos
     */
    public List<ClienteDTO> obtenerTodosLosClientes() {
        log.info("Obteniendo todos los clientes activos");
        List<Cliente> clientes = clienteRepository.findByActivoTrueOrderByNombre();
        return convertirClientesADTO(clientes);
    }

    /**
     * Obtener cliente por ID
     */
    public ClienteDTO obtenerClientePorId(Long id) {
        log.info("Buscando cliente con ID: {}", id);
        Cliente cliente = clienteRepository.findById(id)
            .orElseThrow(() -> new CuentaNotFoundException("Cliente no encontrado con ID: " + id));
        return convertirClienteADTO(cliente);
    }

    /**
     * Crear nuevo cliente
     */
    @Transactional
    public ClienteDTO crearCliente(ClienteDTO clienteDTO) {
        log.info("Creando nuevo cliente con código: {}", clienteDTO.getCodigo());

        if (clienteRepository.existsByCodigo(clienteDTO.getCodigo())) {
            throw new CodigoYaExisteException("Ya existe un cliente con código: " + clienteDTO.getCodigo());
        }

        Cliente cliente = convertirDTOACliente(clienteDTO);
        Cliente clienteGuardado = clienteRepository.save(cliente);

        log.info("Cliente creado exitosamente con ID: {}", clienteGuardado.getId());
        return convertirClienteADTO(clienteGuardado);
    }

    /**
     * Actualizar cliente existente
     */
    @Transactional
    public ClienteDTO actualizarCliente(Long id, ClienteDTO clienteDTO) {
        log.info("Actualizando cliente con ID: {}", id);

        Cliente clienteExistente = clienteRepository.findById(id)
            .orElseThrow(() -> new CuentaNotFoundException("Cliente no encontrado con ID: " + id));

        if (!clienteExistente.getCodigo().equals(clienteDTO.getCodigo()) &&
            clienteRepository.existsByCodigo(clienteDTO.getCodigo())) {
            throw new CodigoYaExisteException("Ya existe un cliente con código: " + clienteDTO.getCodigo());
        }

        actualizarCamposCliente(clienteExistente, clienteDTO);
        Cliente clienteActualizado = clienteRepository.save(clienteExistente);

        log.info("Cliente actualizado exitosamente");
        return convertirClienteADTO(clienteActualizado);
    }

    /**
     * Buscar clientes por nombre
     */
    public List<ClienteDTO> buscarClientesPorNombre(String nombre) {
        log.info("Buscando clientes que contengan: {}", nombre);
        List<Cliente> clientes = clienteRepository.findByNombreContainingIgnoreCaseAndActivoTrue(nombre);
        return convertirClientesADTO(clientes);
    }

    /**
     * Obtener clientes con saldo pendiente
     */
    public List<ClienteDTO> obtenerClientesConSaldo() {
        log.info("Obteniendo clientes con saldo pendiente");
        List<Cliente> clientes = clienteRepository.obtenerClientesConSaldo();
        return convertirClientesADTO(clientes);
    }

    // ========== GESTIÓN DE PROVEEDORES ==========

    /**
     * Obtener todos los proveedores activos
     */
    public List<ProveedorDTO> obtenerTodosLosProveedores() {
        log.info("Obteniendo todos los proveedores activos");
        List<Proveedor> proveedores = proveedorRepository.findByActivoTrueOrderByNombre();
        return convertirProveedoresADTO(proveedores);
    }

    /**
     * Obtener proveedor por ID
     */
    public ProveedorDTO obtenerProveedorPorId(Long id) {
        log.info("Buscando proveedor con ID: {}", id);
        Proveedor proveedor = proveedorRepository.findById(id)
            .orElseThrow(() -> new CuentaNotFoundException("Proveedor no encontrado con ID: " + id));
        return convertirProveedorADTO(proveedor);
    }

    /**
     * Crear nuevo proveedor
     */
    @Transactional
    public ProveedorDTO crearProveedor(ProveedorDTO proveedorDTO) {
        log.info("Creando nuevo proveedor con código: {}", proveedorDTO.getCodigo());

        if (proveedorRepository.existsByCodigo(proveedorDTO.getCodigo())) {
            throw new CodigoYaExisteException("Ya existe un proveedor con código: " + proveedorDTO.getCodigo());
        }

        Proveedor proveedor = convertirDTOAProveedor(proveedorDTO);
        Proveedor proveedorGuardado = proveedorRepository.save(proveedor);

        log.info("Proveedor creado exitosamente con ID: {}", proveedorGuardado.getId());
        return convertirProveedorADTO(proveedorGuardado);
    }

    /**
     * Actualizar proveedor existente
     */
    @Transactional
    public ProveedorDTO actualizarProveedor(Long id, ProveedorDTO proveedorDTO) {
        log.info("Actualizando proveedor con ID: {}", id);

        Proveedor proveedorExistente = proveedorRepository.findById(id)
            .orElseThrow(() -> new CuentaNotFoundException("Proveedor no encontrado con ID: " + id));

        if (!proveedorExistente.getCodigo().equals(proveedorDTO.getCodigo()) &&
            proveedorRepository.existsByCodigo(proveedorDTO.getCodigo())) {
            throw new CodigoYaExisteException("Ya existe un proveedor con código: " + proveedorDTO.getCodigo());
        }

        actualizarCamposProveedor(proveedorExistente, proveedorDTO);
        Proveedor proveedorActualizado = proveedorRepository.save(proveedorExistente);

        log.info("Proveedor actualizado exitosamente");
        return convertirProveedorADTO(proveedorActualizado);
    }

    // ========== CUENTAS POR COBRAR ==========

    /**
     * Crear cuenta por cobrar
     */
    @Transactional
    public CuentaPorCobrarDTO crearCuentaPorCobrar(CuentaPorCobrarDTO cuentaDTO) {
        log.info("Creando cuenta por cobrar para cliente ID: {}", cuentaDTO.getClienteId());

        Cliente cliente = clienteRepository.findById(cuentaDTO.getClienteId())
            .orElseThrow(() -> new CuentaNotFoundException("Cliente no encontrado con ID: " + cuentaDTO.getClienteId()));

        CuentaPorCobrar cuenta = new CuentaPorCobrar();
        cuenta.setCliente(cliente);
        cuenta.setNumeroDocumento(cuentaDTO.getNumeroDocumento());
        cuenta.setFechaEmision(cuentaDTO.getFechaEmision());
        cuenta.setFechaVencimiento(cuentaDTO.getFechaVencimiento());
        cuenta.setMontoOriginal(cuentaDTO.getMontoOriginal());
        cuenta.setDescripcion(cuentaDTO.getDescripcion());
        cuenta.setObservaciones(cuentaDTO.getObservaciones());

        // Actualizar saldo del cliente
        cliente.setSaldoActual(cliente.getSaldoActual().add(cuentaDTO.getMontoOriginal()));
        clienteRepository.save(cliente);

        CuentaPorCobrar cuentaGuardada = cuentaPorCobrarRepository.save(cuenta);
        log.info("Cuenta por cobrar creada exitosamente");

        return convertirCuentaPorCobrarADTO(cuentaGuardada);
    }

    /**
     * Obtener cuentas por cobrar por cliente
     */
    public List<CuentaPorCobrarDTO> obtenerCuentasPorCobrarPorCliente(Long clienteId) {
        log.info("Obteniendo cuentas por cobrar para cliente ID: {}", clienteId);
        List<CuentaPorCobrar> cuentas = cuentaPorCobrarRepository.findByClienteIdOrderByFechaVencimiento(clienteId);
        return convertirCuentasPorCobrarADTO(cuentas);
    }

    /**
     * Obtener cuentas por cobrar vencidas
     */
    public List<CuentaPorCobrarDTO> obtenerCuentasPorCobrarVencidas() {
        log.info("Obteniendo cuentas por cobrar vencidas");
        List<CuentaPorCobrar> cuentas = cuentaPorCobrarRepository.obtenerCuentasVencidas(LocalDate.now());
        return convertirCuentasPorCobrarADTO(cuentas);
    }

    // ========== CUENTAS POR PAGAR ==========

    /**
     * Crear cuenta por pagar
     */
    @Transactional
    public CuentaPorPagarDTO crearCuentaPorPagar(CuentaPorPagarDTO cuentaDTO) {
        log.info("Creando cuenta por pagar para proveedor ID: {}", cuentaDTO.getProveedorId());

        Proveedor proveedor = proveedorRepository.findById(cuentaDTO.getProveedorId())
            .orElseThrow(() -> new CuentaNotFoundException("Proveedor no encontrado con ID: " + cuentaDTO.getProveedorId()));

        CuentaPorPagar cuenta = new CuentaPorPagar();
        cuenta.setProveedor(proveedor);
        cuenta.setNumeroDocumento(cuentaDTO.getNumeroDocumento());
        cuenta.setFechaEmision(cuentaDTO.getFechaEmision());
        cuenta.setFechaVencimiento(cuentaDTO.getFechaVencimiento());
        cuenta.setMontoOriginal(cuentaDTO.getMontoOriginal());
        cuenta.setDescripcion(cuentaDTO.getDescripcion());
        cuenta.setObservaciones(cuentaDTO.getObservaciones());

        // Actualizar saldo del proveedor
        proveedor.setSaldoActual(proveedor.getSaldoActual().add(cuentaDTO.getMontoOriginal()));
        proveedorRepository.save(proveedor);

        CuentaPorPagar cuentaGuardada = cuentaPorPagarRepository.save(cuenta);
        log.info("Cuenta por pagar creada exitosamente");

        return convertirCuentaPorPagarADTO(cuentaGuardada);
    }

    /**
     * Obtener cuentas por pagar vencidas
     */
    public List<CuentaPorPagarDTO> obtenerCuentasPorPagarVencidas() {
        log.info("Obteniendo cuentas por pagar vencidas");
        List<CuentaPorPagar> cuentas = cuentaPorPagarRepository.obtenerCuentasVencidas(LocalDate.now());
        return convertirCuentasPorPagarADTO(cuentas);
    }

    // ========== REPORTES ==========

    /**
     * Generar resumen de clientes y proveedores
     */
    public ResumenClientesProveedoresDTO generarResumen() {
        log.info("Generando resumen de clientes y proveedores");

        List<Cliente> todosLosClientes = clienteRepository.findByActivoTrueOrderByNombre();
        List<Proveedor> todosLosProveedores = proveedorRepository.findByActivoTrueOrderByNombre();
        
        BigDecimal totalCuentasPorCobrar = clienteRepository.obtenerTotalCuentasPorCobrar();
        BigDecimal totalCuentasPorPagar = proveedorRepository.obtenerTotalCuentasPorPagar();
        
        List<CuentaPorCobrar> cuentasVencidasCobrar = cuentaPorCobrarRepository.obtenerCuentasVencidas(LocalDate.now());
        List<CuentaPorPagar> cuentasVencidasPagar = cuentaPorPagarRepository.obtenerCuentasVencidas(LocalDate.now());

        ResumenClientesProveedoresDTO resumen = new ResumenClientesProveedoresDTO();
        resumen.setFecha(LocalDate.now());
        resumen.setEmpresa("Almacén El Planeador - Horacio Porras");
        
        // Datos de clientes
        resumen.setTotalClientes(todosLosClientes.size());
        resumen.setClientesActivos((int) todosLosClientes.stream().filter(Cliente::getActivo).count());
        resumen.setTotalCuentasPorCobrar(totalCuentasPorCobrar != null ? totalCuentasPorCobrar : BigDecimal.ZERO);
        resumen.setCuentasVencidas(cuentasVencidasCobrar.size());
        
        // Datos de proveedores
        resumen.setTotalProveedores(todosLosProveedores.size());
        resumen.setProveedoresActivos((int) todosLosProveedores.stream().filter(Proveedor::getActivo).count());
        resumen.setTotalCuentasPorPagar(totalCuentasPorPagar != null ? totalCuentasPorPagar : BigDecimal.ZERO);
        resumen.setCuentasVencidasPagar(cuentasVencidasPagar.size());
        
        // Cuentas críticas
        resumen.setCuentasPorCobrarVencidas(convertirCuentasPorCobrarADTO(cuentasVencidasCobrar));
        resumen.setCuentasPorPagarVencidas(convertirCuentasPorPagarADTO(cuentasVencidasPagar));

        return resumen;
    }

    // ========== MÉTODOS AUXILIARES ==========

    private List<ClienteDTO> convertirClientesADTO(List<Cliente> clientes) {
        return clientes.stream().map(this::convertirClienteADTO).collect(Collectors.toList());
    }

    private ClienteDTO convertirClienteADTO(Cliente cliente) {
        ClienteDTO dto = new ClienteDTO();
        dto.setId(cliente.getId());
        dto.setCodigo(cliente.getCodigo());
        dto.setNombre(cliente.getNombre());
        dto.setRazonSocial(cliente.getRazonSocial());
        dto.setNit(cliente.getNit());
        dto.setDpi(cliente.getDpi());
        dto.setTelefono(cliente.getTelefono());
        dto.setEmail(cliente.getEmail());
        dto.setDireccion(cliente.getDireccion());
        dto.setTipoCliente(cliente.getTipoCliente());
        dto.setLimiteCredito(cliente.getLimiteCredito());
        dto.setSaldoActual(cliente.getSaldoActual());
        dto.setDiasCredito(cliente.getDiasCredito());
        dto.setActivo(cliente.getActivo());
        dto.setObservaciones(cliente.getObservaciones());
        dto.setFechaCreacion(cliente.getFechaCreacion());
        dto.setFechaModificacion(cliente.getFechaModificacion());
        dto.calcularCampos();
        return dto;
    }

    private Cliente convertirDTOACliente(ClienteDTO dto) {
        Cliente cliente = new Cliente();
        cliente.setCodigo(dto.getCodigo());
        cliente.setNombre(dto.getNombre());
        cliente.setRazonSocial(dto.getRazonSocial());
        cliente.setNit(dto.getNit());
        cliente.setDpi(dto.getDpi());
        cliente.setTelefono(dto.getTelefono());
        cliente.setEmail(dto.getEmail());
        cliente.setDireccion(dto.getDireccion());
        cliente.setTipoCliente(dto.getTipoCliente() != null ? dto.getTipoCliente() : Cliente.TipoCliente.CONSUMIDOR_FINAL);
        cliente.setLimiteCredito(dto.getLimiteCredito() != null ? dto.getLimiteCredito() : BigDecimal.ZERO);
        cliente.setSaldoActual(dto.getSaldoActual() != null ? dto.getSaldoActual() : BigDecimal.ZERO);
        cliente.setDiasCredito(dto.getDiasCredito() != null ? dto.getDiasCredito() : 0);
        cliente.setActivo(dto.getActivo() != null ? dto.getActivo() : true);
        cliente.setObservaciones(dto.getObservaciones());
        return cliente;
    }

    private void actualizarCamposCliente(Cliente cliente, ClienteDTO dto) {
        cliente.setCodigo(dto.getCodigo());
        cliente.setNombre(dto.getNombre());
        cliente.setRazonSocial(dto.getRazonSocial());
        cliente.setNit(dto.getNit());
        cliente.setDpi(dto.getDpi());
        cliente.setTelefono(dto.getTelefono());
        cliente.setEmail(dto.getEmail());
        cliente.setDireccion(dto.getDireccion());
        cliente.setTipoCliente(dto.getTipoCliente());
        cliente.setLimiteCredito(dto.getLimiteCredito());
        cliente.setDiasCredito(dto.getDiasCredito());
        cliente.setActivo(dto.getActivo());
        cliente.setObservaciones(dto.getObservaciones());
    }

    private List<ProveedorDTO> convertirProveedoresADTO(List<Proveedor> proveedores) {
        return proveedores.stream().map(this::convertirProveedorADTO).collect(Collectors.toList());
    }

    private ProveedorDTO convertirProveedorADTO(Proveedor proveedor) {
        ProveedorDTO dto = new ProveedorDTO();
        dto.setId(proveedor.getId());
        dto.setCodigo(proveedor.getCodigo());
        dto.setNombre(proveedor.getNombre());
        dto.setRazonSocial(proveedor.getRazonSocial());
        dto.setNit(proveedor.getNit());
        dto.setTelefono(proveedor.getTelefono());
        dto.setEmail(proveedor.getEmail());
        dto.setDireccion(proveedor.getDireccion());
        dto.setContacto(proveedor.getContacto());
        dto.setTipoProveedor(proveedor.getTipoProveedor());
        dto.setSaldoActual(proveedor.getSaldoActual());
        dto.setDiasPago(proveedor.getDiasPago());
        dto.setActivo(proveedor.getActivo());
        dto.setObservaciones(proveedor.getObservaciones());
        dto.setFechaCreacion(proveedor.getFechaCreacion());
        dto.setFechaModificacion(proveedor.getFechaModificacion());
        dto.calcularCampos();
        return dto;
    }

    private Proveedor convertirDTOAProveedor(ProveedorDTO dto) {
        Proveedor proveedor = new Proveedor();
        proveedor.setCodigo(dto.getCodigo());
        proveedor.setNombre(dto.getNombre());
        proveedor.setRazonSocial(dto.getRazonSocial());
        proveedor.setNit(dto.getNit());
        proveedor.setTelefono(dto.getTelefono());
        proveedor.setEmail(dto.getEmail());
        proveedor.setDireccion(dto.getDireccion());
        proveedor.setContacto(dto.getContacto());
        proveedor.setTipoProveedor(dto.getTipoProveedor() != null ? dto.getTipoProveedor() : Proveedor.TipoProveedor.MERCADERIAS);
        proveedor.setSaldoActual(dto.getSaldoActual() != null ? dto.getSaldoActual() : BigDecimal.ZERO);
        proveedor.setDiasPago(dto.getDiasPago() != null ? dto.getDiasPago() : 30);
        proveedor.setActivo(dto.getActivo() != null ? dto.getActivo() : true);
        proveedor.setObservaciones(dto.getObservaciones());
        return proveedor;
    }

    private void actualizarCamposProveedor(Proveedor proveedor, ProveedorDTO dto) {
        proveedor.setCodigo(dto.getCodigo());
        proveedor.setNombre(dto.getNombre());
        proveedor.setRazonSocial(dto.getRazonSocial());
        proveedor.setNit(dto.getNit());
        proveedor.setTelefono(dto.getTelefono());
        proveedor.setEmail(dto.getEmail());
        proveedor.setDireccion(dto.getDireccion());
        proveedor.setContacto(dto.getContacto());
        proveedor.setTipoProveedor(dto.getTipoProveedor());
        proveedor.setDiasPago(dto.getDiasPago());
        proveedor.setActivo(dto.getActivo());
        proveedor.setObservaciones(dto.getObservaciones());
    }

    private List<CuentaPorCobrarDTO> convertirCuentasPorCobrarADTO(List<CuentaPorCobrar> cuentas) {
        return cuentas.stream().map(this::convertirCuentaPorCobrarADTO).collect(Collectors.toList());
    }

    private CuentaPorCobrarDTO convertirCuentaPorCobrarADTO(CuentaPorCobrar cuenta) {
        CuentaPorCobrarDTO dto = new CuentaPorCobrarDTO();
        dto.setId(cuenta.getId());
        dto.setClienteId(cuenta.getCliente().getId());
        dto.setClienteNombre(cuenta.getCliente().getNombre());
        dto.setNumeroDocumento(cuenta.getNumeroDocumento());
        dto.setFechaEmision(cuenta.getFechaEmision());
        dto.setFechaVencimiento(cuenta.getFechaVencimiento());
        dto.setMontoOriginal(cuenta.getMontoOriginal());
        dto.setMontoAbonado(cuenta.getMontoAbonado());
        dto.setSaldoPendiente(cuenta.getSaldoPendiente());
        dto.setEstado(cuenta.getEstado());
        dto.setDescripcion(cuenta.getDescripcion());
        dto.setObservaciones(cuenta.getObservaciones());
        dto.setFechaCreacion(cuenta.getFechaCreacion());
        dto.setFechaModificacion(cuenta.getFechaModificacion());
        dto.calcularCampos();
        return dto;
    }

    private List<CuentaPorPagarDTO> convertirCuentasPorPagarADTO(List<CuentaPorPagar> cuentas) {
        return cuentas.stream().map(this::convertirCuentaPorPagarADTO).collect(Collectors.toList());
    }

    private CuentaPorPagarDTO convertirCuentaPorPagarADTO(CuentaPorPagar cuenta) {
        CuentaPorPagarDTO dto = new CuentaPorPagarDTO();
        dto.setId(cuenta.getId());
        dto.setProveedorId(cuenta.getProveedor().getId());
        dto.setProveedorNombre(cuenta.getProveedor().getNombre());
        dto.setNumeroDocumento(cuenta.getNumeroDocumento());
        dto.setFechaEmision(cuenta.getFechaEmision());
        dto.setFechaVencimiento(cuenta.getFechaVencimiento());
        dto.setMontoOriginal(cuenta.getMontoOriginal());
        dto.setMontoAbonado(cuenta.getMontoAbonado());
        dto.setSaldoPendiente(cuenta.getSaldoPendiente());
        dto.setEstado(cuenta.getEstado());
        dto.setDescripcion(cuenta.getDescripcion());
        dto.setObservaciones(cuenta.getObservaciones());
        dto.setFechaCreacion(cuenta.getFechaCreacion());
        dto.setFechaModificacion(cuenta.getFechaModificacion());
        dto.calcularCampos();
        return dto;
    }
}