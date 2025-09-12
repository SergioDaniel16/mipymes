// src/app/core/services/asiento-contable.service.ts - Servicio para asientos contables
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ApiService } from './api.service';
import { AsientoContable } from '../models/asiento-contable.model';
import { HttpParams } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class AsientoContableService {

  constructor(private apiService: ApiService) {}

  // Obtener libro diario completo
  obtenerLibroDiario(): Observable<AsientoContable[]> {
    return this.apiService.get<AsientoContable[]>('asientos');
  }

  // Obtener libro diario por período
  obtenerLibroDiarioPorPeriodo(fechaInicio: string, fechaFin: string): Observable<AsientoContable[]> {
    const params = new HttpParams()
      .set('fechaInicio', fechaInicio)
      .set('fechaFin', fechaFin);
    return this.apiService.get<AsientoContable[]>('asientos/periodo', params);
  }

  // Obtener asiento por ID
  obtenerAsientoPorId(id: number): Observable<AsientoContable> {
    return this.apiService.get<AsientoContable>(`asientos/${id}`);
  }

  // Obtener asiento por número
  obtenerAsientoPorNumero(numero: number): Observable<AsientoContable> {
    return this.apiService.get<AsientoContable>(`asientos/numero/${numero}`);
  }

  // Buscar asientos por descripción
  buscarAsientosPorDescripcion(descripcion: string): Observable<AsientoContable[]> {
    const params = new HttpParams().set('descripcion', descripcion);
    return this.apiService.get<AsientoContable[]>('asientos/buscar', params);
  }

  // Crear nuevo asiento
  crearAsiento(asiento: AsientoContable): Observable<AsientoContable> {
    return this.apiService.post<AsientoContable>('asientos', asiento);
  }

  // Contabilizar asiento
  contabilizarAsiento(id: number): Observable<AsientoContable> {
    return this.apiService.put<AsientoContable>(`asientos/${id}/contabilizar`, {});
  }
}
