// src/app/core/models/balance.model.ts - Modelos para balance
export interface BalanceComprobacion {
  fechaCorte: string;
  empresa: string;
  periodo: string;
  lineas: LineaBalance[];
  totales: TotalesBalance;
  balanceado: boolean;
}

export interface LineaBalance {
  codigo: string;
  nombreCuenta: string;
  tipoCuenta: string;
  naturaleza: string;
  saldoDeudor: number;
  saldoAcreedor: number;
}

export interface TotalesBalance {
  totalDeudores: number;
  totalAcreedores: number;
  diferencia: number;
  totalCuentas: number;
  cuentasDeudoras: number;
  cuentasAcreedoras: number;
}
