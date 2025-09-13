// src/app/core/services/asiento-contable.service.ts - Servicio corregido
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient, HttpParams } from '@angular/common/http';
import { catchError, map } from 'rxjs/operators';
import { environment } from '../../../environments/environment';

// Interfaces para el servicio
export interface AsientoContableDTO {
  id?: number;
  numeroAsiento?: number;
  fecha: string;
  descripcion: string;
  referencia?: string;
  tipo: TipoAsiento;
  totalDebitos: number;
  totalCreditos: number;
  estado?: EstadoAsiento;
  movimientos: MovimientoContableDTO[];
  fechaCreacion?: string;
  fechaModificacion?: string;
  creadoPor?: string;
  tipoDescripcion?: string;
  estadoDescripcion?: string;
  balanceado?: boolean;
}

export interface MovimientoContableDTO {
  id?: number;
  cuentaId: number;
  cuentaCodigo?: string;
  cuentaNombre?: string;
  tipoMovimiento: TipoMovimiento;
  monto: number;
  descripcion?: string;
  orden?: number;
  fechaCreacion?: string;
  fechaModificacion?: string;
  tipoMovimientoDescripcion?: string;
}

export enum TipoAsiento {
  APERTURA = 'APERTURA',
  OPERACION = 'OPERACION',
  AJUSTE = 'AJUSTE',
  CIERRE = 'CIERRE'
}

export enum EstadoAsiento {
  BORRADOR = 'BORRADOR',
  VALIDADO = 'VALIDADO',
  CONTABILIZADO = 'CONTABILIZADO',
  ANULADO = 'ANULADO'
}

export enum TipoMovimiento {
  DEBITO = 'DEBITO',
  CREDITO = 'CREDITO'
}

interface ApiResponse<T> {
  success: boolean;
  message: string;
  data: T;
}

@Injectable({
  providedIn: 'root'
})
export class AsientoContableService {
  private readonly baseUrl = `${environment.apiUrl}/asientos`;

  constructor(private http: HttpClient) {}

  /**
   * Obtener libro diario completo
   */
  obtenerLibroDiario(): Observable<AsientoContableDTO[]> {
    return this.http.get<ApiResponse<AsientoContableDTO[]>>(this.baseUrl)
      .pipe(
        map(response => response.data),
        catchError(this.handleError)
      );
  }

  /**
   * Obtener libro diario por período
   */
  obtenerLibroDiarioPorPeriodo(fechaInicio: string, fechaFin: string): Observable<AsientoContableDTO[]> {
    const params = new HttpParams()
      .set('fechaInicio', fechaInicio)
      .set('fechaFin', fechaFin);
    return this.http.get<ApiResponse<AsientoContableDTO[]>>(`${this.baseUrl}/periodo`, { params })
      .pipe(
        map(response => response.data),
        catchError(this.handleError)
      );
  }

  /**
   * Obtener asiento por ID
   */
  obtenerAsientoPorId(id: number): Observable<AsientoContableDTO> {
    return this.http.get<ApiResponse<AsientoContableDTO>>(`${this.baseUrl}/${id}`)
      .pipe(
        map(response => response.data),
        catchError(this.handleError)
      );
  }

  /**
   * Obtener asiento por número
   */
  obtenerAsientoPorNumero(numero: number): Observable<AsientoContableDTO> {
    return this.http.get<ApiResponse<AsientoContableDTO>>(`${this.baseUrl}/numero/${numero}`)
      .pipe(
        map(response => response.data),
        catchError(this.handleError)
      );
  }

  /**
   * Buscar asientos por descripción
   */
  buscarAsientosPorDescripcion(descripcion: string): Observable<AsientoContableDTO[]> {
    const params = new HttpParams().set('descripcion', descripcion);
    return this.http.get<ApiResponse<AsientoContableDTO[]>>(`${this.baseUrl}/buscar`, { params })
      .pipe(
        map(response => response.data),
        catchError(this.handleError)
      );
  }

  /**
   * Crear nuevo asiento
   */
  crearAsiento(asiento: AsientoContableDTO): Observable<AsientoContableDTO> {
    return this.http.post<ApiResponse<AsientoContableDTO>>(this.baseUrl, asiento)
      .pipe(
        map(response => response.data),
        catchError(this.handleError)
      );
  }

  /**
   * Contabilizar asiento
   */
  contabilizarAsiento(id: number): Observable<AsientoContableDTO> {
    return this.http.put<ApiResponse<AsientoContableDTO>>(`${this.baseUrl}/${id}/contabilizar`, {})
      .pipe(
        map(response => response.data),
        catchError(this.handleError)
      );
  }

  /**
   * Manejo de errores
   */
  private handleError = (error: any): Observable<never> => {
    console.error('Error en AsientoContableService:', error);
    throw error;
  }

  /**
   * Funciones helper para obtener descripciones
   */
  static getTipoAsientoDescripcion(tipo: TipoAsiento): string {
    const descripciones = {
      [TipoAsiento.APERTURA]: 'Asiento de Apertura',
      [TipoAsiento.OPERACION]: 'Operación Normal',
      [TipoAsiento.AJUSTE]: 'Asiento de Ajuste',
      [TipoAsiento.CIERRE]: 'Asiento de Cierre'
    };
    return descripciones[tipo] || tipo;
  }

  static getEstadoAsientoDescripcion(estado: EstadoAsiento): string {
    const descripciones = {
      [EstadoAsiento.BORRADOR]: 'Borrador',
      [EstadoAsiento.VALIDADO]: 'Validado',
      [EstadoAsiento.CONTABILIZADO]: 'Contabilizado',
      [EstadoAsiento.ANULADO]: 'Anulado'
    };
    return descripciones[estado] || estado;
  }

  static getTipoMovimientoDescripcion(tipo: TipoMovimiento): string {
    const descripciones = {
      [TipoMovimiento.DEBITO]: 'Débito',
      [TipoMovimiento.CREDITO]: 'Crédito'
    };
    return descripciones[tipo] || tipo;
  }

  /**
   * Validar que un asiento esté balanceado
   */
  static validarAsientoBalanceado(asiento: AsientoContableDTO): boolean {
    return Math.abs(asiento.totalDebitos - asiento.totalCreditos) < 0.01;
  }

  /**
   * Calcular totales de un asiento
   */
  static calcularTotales(movimientos: MovimientoContableDTO[]): { debitos: number, creditos: number } {
    const debitos = movimientos
      .filter(m => m.tipoMovimiento === TipoMovimiento.DEBITO)
      .reduce((sum, m) => sum + m.monto, 0);

    const creditos = movimientos
      .filter(m => m.tipoMovimiento === TipoMovimiento.CREDITO)
      .reduce((sum, m) => sum + m.monto, 0);

    return { debitos, creditos };
  }
}
