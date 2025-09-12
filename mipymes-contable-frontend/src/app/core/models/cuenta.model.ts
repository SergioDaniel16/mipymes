// Modelos para cuentas contables - coinciden con el backend Spring Boot
export interface Cuenta {
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
