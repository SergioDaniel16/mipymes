// src/app/core/models/cuenta.model.ts - Modelo corregido para coincidir con backend
export interface CuentaDTO {
  id?: number;
  codigo: string;
  nombre: string;
  tipo: TipoCuenta;
  naturaleza: NaturalezaCuenta;
  saldo: number;
  activa?: boolean;
  descripcion?: string;
  fechaCreacion?: string;
  fechaModificacion?: string;
  // Campos calculados para la vista
  tipoDescripcion?: string;
  naturalezaDescripcion?: string;
}

export enum TipoCuenta {
  ACTIVO = 'ACTIVO',
  PASIVO = 'PASIVO',
  PATRIMONIO = 'PATRIMONIO',
  INGRESO = 'INGRESO',
  GASTO = 'GASTO'
}

export enum NaturalezaCuenta {
  DEUDORA = 'DEUDORA',
  ACREEDORA = 'ACREEDORA'
}

// Funciones helper para obtener descripciones
export function getTipoDescripcion(tipo: TipoCuenta): string {
  const descripciones = {
    [TipoCuenta.ACTIVO]: 'Activo',
    [TipoCuenta.PASIVO]: 'Pasivo',
    [TipoCuenta.PATRIMONIO]: 'Patrimonio',
    [TipoCuenta.INGRESO]: 'Ingreso',
    [TipoCuenta.GASTO]: 'Gasto'
  };
  return descripciones[tipo] || tipo;
}

export function getNaturalezaDescripcion(naturaleza: NaturalezaCuenta): string {
  const descripciones = {
    [NaturalezaCuenta.DEUDORA]: 'Deudora - Aumenta con Débitos',
    [NaturalezaCuenta.ACREEDORA]: 'Acreedora - Aumenta con Créditos'
  };
  return descripciones[naturaleza] || naturaleza;
}
