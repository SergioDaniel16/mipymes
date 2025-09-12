// src/app/core/services/cuenta.service.ts - Servicio para cuentas
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ApiService } from './api.service';
import { Cuenta, TipoCuenta } from '../models/cuenta.model';
import { HttpParams } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class CuentaService {

  constructor(private apiService: ApiService) {}

  // Obtener todas las cuentas
  obtenerTodasLasCuentas(): Observable<Cuenta[]> {
    return this.apiService.get<Cuenta[]>('cuentas');
  }

  // Obtener cuenta por ID
  obtenerCuentaPorId(id: number): Observable<Cuenta> {
    return this.apiService.get<Cuenta>(`cuentas/${id}`);
  }

  // Obtener cuenta por código
  obtenerCuentaPorCodigo(codigo: string): Observable<Cuenta> {
    return this.apiService.get<Cuenta>(`cuentas/codigo/${codigo}`);
  }

  // Obtener cuentas por tipo
  obtenerCuentasPorTipo(tipo: TipoCuenta): Observable<Cuenta[]> {
    return this.apiService.get<Cuenta[]>(`cuentas/tipo/${tipo}`);
  }

  // Buscar cuentas por nombre
  buscarCuentasPorNombre(nombre: string): Observable<Cuenta[]> {
    const params = new HttpParams().set('nombre', nombre);
    return this.apiService.get<Cuenta[]>('cuentas/buscar', params);
  }

  // Crear nueva cuenta
  crearCuenta(cuenta: Cuenta): Observable<Cuenta> {
    return this.apiService.post<Cuenta>('cuentas', cuenta);
  }

  // Actualizar cuenta
  actualizarCuenta(id: number, cuenta: Cuenta): Observable<Cuenta> {
    return this.apiService.put<Cuenta>(`cuentas/${id}`, cuenta);
  }

  // Desactivar cuenta
  desactivarCuenta(id: number): Observable<any> {
    return this.apiService.delete<any>(`cuentas/${id}`);
  }

  // Obtener catálogo de cuentas
  obtenerCatalogoCuentas(): Observable<Cuenta[]> {
    return this.apiService.get<Cuenta[]>('cuentas/catalogo');
  }
}
