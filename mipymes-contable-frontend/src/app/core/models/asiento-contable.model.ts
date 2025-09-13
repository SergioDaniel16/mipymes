// src/app/core/models/asiento-contable.model.ts - Modelo actualizado
export interface AsientoContableDTO {
  id?: number;
  numeroAsiento?: number;
  fecha: string;
  descripcion: string;
  referencia?: string;
  tipo: TipoAsiento;
  totalDebitos: number;
  totalCreditos: number;
  estado?: EstadoAsiento;
  movimientos: MovimientoContableDTO[];
  fechaCreacion?: string;
  fechaModificacion?: string;
  creadoPor?: string;
  tipoDescripcion?: string;
  estadoDescripcion?: string;
  balanceado?: boolean;
}

export interface MovimientoContableDTO {
  id?: number;
  cuentaId: number;
  cuentaCodigo?: string;
  cuentaNombre?: string;
  tipoMovimiento: TipoMovimiento;
  monto: number;
  descripcion?: string;
  orden?: number;
  fechaCreacion?: string;
  fechaModificacion?: string;
  tipoMovimientoDescripcion?: string;
}

export enum TipoAsiento {
  APERTURA = 'APERTURA',
  OPERACION = 'OPERACION',
  AJUSTE = 'AJUSTE',
  CIERRE = 'CIERRE'
}

export enum EstadoAsiento {
  BORRADOR = 'BORRADOR',
  VALIDADO = 'VALIDADO',
  CONTABILIZADO = 'CONTABILIZADO',
  ANULADO = 'ANULADO'
}

export enum TipoMovimiento {
  DEBITO = 'DEBITO',
  CREDITO = 'CREDITO'
}

// Interfaces para crear asientos
export interface CrearAsientoDTO {
  fecha: string;
  descripcion: string;
  referencia?: string;
  tipo: TipoAsiento;
  movimientos: CrearMovimientoDTO[];
  creadoPor?: string;
}

export interface CrearMovimientoDTO {
  cuentaId: number;
  tipoMovimiento: TipoMovimiento;
  monto: number;
  descripcion?: string;
  orden?: number;
}

// Funciones helper
export function getTipoAsientoDescripcion(tipo: TipoAsiento): string {
  const descripciones = {
    [TipoAsiento.APERTURA]: 'Asiento de Apertura',
    [TipoAsiento.OPERACION]: 'Operación Normal',
    [TipoAsiento.AJUSTE]: 'Asiento de Ajuste',
    [TipoAsiento.CIERRE]: 'Asiento de Cierre'
  };
  return descripciones[tipo] || tipo;
}

export function getEstadoAsientoDescripcion(estado: EstadoAsiento): string {
  const descripciones = {
    [EstadoAsiento.BORRADOR]: 'Borrador',
    [EstadoAsiento.VALIDADO]: 'Validado',
    [EstadoAsiento.CONTABILIZADO]: 'Contabilizado',
    [EstadoAsiento.ANULADO]: 'Anulado'
  };
  return descripciones[estado] || estado;
}

export function getTipoMovimientoDescripcion(tipo: TipoMovimiento): string {
  const descripciones = {
    [TipoMovimiento.DEBITO]: 'Débito',
    [TipoMovimiento.CREDITO]: 'Crédito'
  };
  return descripciones[tipo] || tipo;
}

export function getEstadoColor(estado: EstadoAsiento): string {
  const colores = {
    [EstadoAsiento.BORRADOR]: 'warn',
    [EstadoAsiento.VALIDADO]: 'accent',
    [EstadoAsiento.CONTABILIZADO]: 'primary',
    [EstadoAsiento.ANULADO]: ''
  };
  return colores[estado] || '';
}
