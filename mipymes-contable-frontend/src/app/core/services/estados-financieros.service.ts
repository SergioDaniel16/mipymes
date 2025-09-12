// src/app/core/services/estados-financieros.service.ts - Servicio para estados financieros
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ApiService } from './api.service';
import { BalanceGeneral, EstadoResultados } from '../models/estados-financieros.model';
import { HttpParams } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class EstadosFinancierosService {

  constructor(private apiService: ApiService) {}

  // Balance General actual
  generarBalanceGeneral(): Observable<BalanceGeneral> {
    return this.apiService.get<BalanceGeneral>('estados-financieros/balance-general');
  }

  // Balance General por fecha
  generarBalanceGeneralPorFecha(fecha: string): Observable<BalanceGeneral> {
    return this.apiService.get<BalanceGeneral>(`estados-financieros/balance-general/${fecha}`);
  }

  // Estado de Resultados anual
  generarEstadoResultadosAnual(): Observable<EstadoResultados> {
    return this.apiService.get<EstadoResultados>('estados-financieros/estado-resultados');
  }

  // Estado de Resultados por período
  generarEstadoResultadosPorPeriodo(fechaInicio: string, fechaFin: string): Observable<EstadoResultados> {
    const params = new HttpParams()
      .set('fechaInicio', fechaInicio)
      .set('fechaFin', fechaFin);
    return this.apiService.get<EstadoResultados>('estados-financieros/estado-resultados/periodo', params);
  }
}
