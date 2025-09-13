// src/app/core/services/estados-financieros.service.ts
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ApiService } from './api.service';
import { HttpParams } from '@angular/common/http';

// Interfaces para los DTOs
export interface BalanceGeneralDTO {
  fechaCorte: string;
  empresa: string;
  periodo: string;
  activosCorrientes: LineaEstadoDTO[];
  activosNoCorrientes: LineaEstadoDTO[];
  totalActivos: number;
  pasivosCorrientes: LineaEstadoDTO[];
  pasivosNoCorrientes: LineaEstadoDTO[];
  totalPasivos: number;
  patrimonio: LineaEstadoDTO[];
  totalPatrimonio: number;
  balanceado: boolean;
  diferencia: number;
}

export interface EstadoResultadosDTO {
  fechaInicio: string;
  fechaFin: string;
  empresa: string;
  periodo: string;
  ingresos: LineaEstadoDTO[];
  totalIngresos: number;
  costos: LineaEstadoDTO[];
  totalCostos: number;
  utilidadBruta: number;
  gastos: LineaEstadoDTO[];
  totalGastos: number;
  utilidadNeta: number;
  tipoResultado: string;
}

export interface LineaEstadoDTO {
  codigo: string;
  nombreCuenta: string;
  tipoCuenta: string;
  naturaleza: string;
  saldoDeudor: number;
  saldoAcreedor: number;
  monto?: number; // Para Estados de Resultados
}

@Injectable({
  providedIn: 'root'
})
export class EstadosFinancierosService {

  constructor(private apiService: ApiService) {}

  /**
   * Generar Balance General actual
   */
  generarBalanceGeneral(): Observable<BalanceGeneralDTO> {
    return this.apiService.get<BalanceGeneralDTO>('estados-financieros/balance-general');
  }

  /**
   * Generar Balance General por fecha específica
   */
  generarBalanceGeneralPorFecha(fecha: string): Observable<BalanceGeneralDTO> {
    return this.apiService.get<BalanceGeneralDTO>(`estados-financieros/balance-general/${fecha}`);
  }

  /**
   * Generar Estado de Resultados anual
   */
  generarEstadoResultadosAnual(): Observable<EstadoResultadosDTO> {
    return this.apiService.get<EstadoResultadosDTO>('estados-financieros/estado-resultados');
  }

  /**
   * Generar Estado de Resultados por período
   */
  generarEstadoResultadosPorPeriodo(fechaInicio: string, fechaFin: string): Observable<EstadoResultadosDTO> {
    const params = new HttpParams()
      .set('fechaInicio', fechaInicio)
      .set('fechaFin', fechaFin);
    return this.apiService.get<EstadoResultadosDTO>('estados-financieros/estado-resultados/periodo', params);
  }
}
