// src/app/core/models/asiento-contable.model.ts - Modelos para asientos
export interface AsientoContable {
  id?: number;
  numeroAsiento?: number;
  fecha: string;
  descripcion: string;
  referencia?: string;
  tipo: TipoAsiento;
  totalDebitos: number;
  totalCreditos: number;
  estado?: EstadoAsiento;
  movimientos: MovimientoContable[];
  fechaCreacion?: string;
  fechaModificacion?: string;
  creadoPor?: string;
  tipoDescripcion?: string;
  estadoDescripcion?: string;
  balanceado?: boolean;
}

export interface MovimientoContable {
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
