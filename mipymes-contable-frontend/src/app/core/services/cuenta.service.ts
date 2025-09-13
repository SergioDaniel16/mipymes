// src/app/core/services/cuenta.service.ts - Servicio corregido para conectar con backend
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient, HttpParams } from '@angular/common/http';
import { catchError, map } from 'rxjs/operators';
import { CuentaDTO } from '../models/cuenta.model';
import { TipoCuenta } from '../models/cuenta.model';
import { environment } from '../../../environments/environment';

interface ApiResponse<T> {
  success: boolean;
  message: string;
  data: T;
}

@Injectable({
  providedIn: 'root'
})
export class CuentaService {
  private readonly baseUrl = `${environment.apiUrl}/cuentas`;

  constructor(private http: HttpClient) {}

  /**
   * Obtener todas las cuentas activas
   */
  obtenerTodasLasCuentas(): Observable<CuentaDTO[]> {
    return this.http.get<ApiResponse<CuentaDTO[]>>(this.baseUrl)
      .pipe(
        map(response => response.data),
        catchError(this.handleError)
      );
  }

  /**
   * Obtener cuenta por ID
   */
  obtenerCuentaPorId(id: number): Observable<CuentaDTO> {
    return this.http.get<ApiResponse<CuentaDTO>>(`${this.baseUrl}/${id}`)
      .pipe(
        map(response => response.data),
        catchError(this.handleError)
      );
  }

  /**
   * Obtener cuenta por código
   */
  obtenerCuentaPorCodigo(codigo: string): Observable<CuentaDTO> {
    return this.http.get<ApiResponse<CuentaDTO>>(`${this.baseUrl}/codigo/${codigo}`)
      .pipe(
        map(response => response.data),
        catchError(this.handleError)
      );
  }

  /**
   * Obtener cuentas por tipo
   */
  obtenerCuentasPorTipo(tipo: TipoCuenta): Observable<CuentaDTO[]> {
    return this.http.get<ApiResponse<CuentaDTO[]>>(`${this.baseUrl}/tipo/${tipo}`)
      .pipe(
        map(response => response.data),
        catchError(this.handleError)
      );
  }

  /**
   * Buscar cuentas por nombre
   */
  buscarCuentasPorNombre(nombre: string): Observable<CuentaDTO[]> {
    const params = new HttpParams().set('nombre', nombre);
    return this.http.get<ApiResponse<CuentaDTO[]>>(`${this.baseUrl}/buscar`, { params })
      .pipe(
        map(response => response.data),
        catchError(this.handleError)
      );
  }

  /**
   * Crear nueva cuenta
   */
  crearCuenta(cuenta: CuentaDTO): Observable<CuentaDTO> {
    return this.http.post<ApiResponse<CuentaDTO>>(this.baseUrl, cuenta)
      .pipe(
        map(response => response.data),
        catchError(this.handleError)
      );
  }

  /**
   * Actualizar cuenta existente
   */
  actualizarCuenta(id: number, cuenta: CuentaDTO): Observable<CuentaDTO> {
    return this.http.put<ApiResponse<CuentaDTO>>(`${this.baseUrl}/${id}`, cuenta)
      .pipe(
        map(response => response.data),
        catchError(this.handleError)
      );
  }

  /**
   * Desactivar cuenta
   */
  desactivarCuenta(id: number): Observable<void> {
    return this.http.delete<ApiResponse<void>>(`${this.baseUrl}/${id}`)
      .pipe(
        map(response => response.data),
        catchError(this.handleError)
      );
  }

  /**
   * Obtener catálogo de cuentas
   */
  obtenerCatalogoCuentas(): Observable<CuentaDTO[]> {
    return this.http.get<ApiResponse<CuentaDTO[]>>(`${this.baseUrl}/catalogo`)
      .pipe(
        map(response => response.data),
        catchError(this.handleError)
      );
  }

  /**
   * Manejo de errores
   */
  private handleError = (error: any): Observable<never> => {
    console.error('Error en CuentaService:', error);
    throw error;
  }
}
