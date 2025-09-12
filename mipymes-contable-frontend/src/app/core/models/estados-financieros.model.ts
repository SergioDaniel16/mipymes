
// src/app/core/models/estados-financieros.model.ts - Estados financieros
export interface BalanceGeneral {
  fechaCorte: string;
  empresa: string;
  periodo: string;
  activosCorrientes: LineaBalance[];
  activosNoCorrientes: LineaBalance[];
  totalActivos: number;
  pasivosCorrientes: LineaBalance[];
  pasivosNoCorrientes: LineaBalance[];
  totalPasivos: number;
  patrimonio: LineaBalance[];
  totalPatrimonio: number;
  balanceado: boolean;
  diferencia: number;
}

export interface EstadoResultados {
  fechaInicio: string;
  fechaFin: string;
  empresa: string;
  periodo: string;
  ingresos: LineaBalance[];
  totalIngresos: number;
  costos: LineaBalance[];
  totalCostos: number;
  utilidadBruta: number;
  gastos: LineaBalance[];
  totalGastos: number;
  utilidadNeta: number;
  tipoResultado: string;
}
