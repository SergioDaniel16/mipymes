// src/app/core/services/balance.service.ts - Servicio para balance de comprobación
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ApiService } from './api.service';
import { BalanceComprobacion } from '../models/balance.model';
import { TipoCuenta } from '../models/cuenta.model';

@Injectable({
  providedIn: 'root'
})
export class BalanceService {

  constructor(private apiService: ApiService) {}

  // Generar balance de comprobación actual
  generarBalanceActual(): Observable<BalanceComprobacion> {
    return this.apiService.get<BalanceComprobacion>('balance-comprobacion');
  }

  // Generar balance por fecha
  generarBalancePorFecha(fecha: string): Observable<BalanceComprobacion> {
    return this.apiService.get<BalanceComprobacion>(`balance-comprobacion/fecha/${fecha}`);
  }

  // Generar balance con saldo únicamente
  generarBalanceConSaldo(): Observable<BalanceComprobacion> {
    return this.apiService.get<BalanceComprobacion>('balance-comprobacion/con-saldo');
  }

  // Generar balance por tipo de cuenta
  generarBalancePorTipo(tipo: TipoCuenta): Observable<BalanceComprobacion> {
    return this.apiService.get<BalanceComprobacion>(`balance-comprobacion/tipo/${tipo}`);
  }
}
