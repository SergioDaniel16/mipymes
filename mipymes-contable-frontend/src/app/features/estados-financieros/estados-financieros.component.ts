// src/app/features/estados-financieros/estados-financieros.component.ts
import { Component, OnInit } from '@angular/core';
import { EstadosFinancierosService, BalanceGeneralDTO, EstadoResultadosDTO, LineaEstadoDTO } from '../../core/services/estados-financieros.service';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-estados-financieros',
  template: `
    <div class="page-container">
      <div class="page-header">
        <h1>Estados Financieros</h1>
        <div class="header-actions">
          <button mat-raised-button color="primary" (click)="generarEstados()" [disabled]="cargando">
            <mat-icon>refresh</mat-icon>
            {{ cargando ? 'Actualizando...' : 'Actualizar' }}
          </button>
          <button mat-stroked-button (click)="exportarPDF()" [disabled]="cargando">
            <mat-icon>picture_as_pdf</mat-icon>
            Exportar PDF
          </button>
        </div>
      </div>

      <!-- Indicador de carga -->
      <div *ngIf="cargando" class="loading-container">
        <mat-spinner diameter="40"></mat-spinner>
        <p>Generando estados financieros...</p>
      </div>

      <!-- Mensaje de error -->
      <div *ngIf="error" class="error-container">
        <mat-icon color="warn">error</mat-icon>
        <p>{{ error }}</p>
        <button mat-button color="primary" (click)="generarEstados()">Reintentar</button>
      </div>

      <!-- Pestañas para cambiar entre estados -->
      <mat-tab-group *ngIf="!cargando && !error">
        <!-- Balance General -->
        <mat-tab label="Balance General">
          <div class="tab-content" *ngIf="balanceGeneral">
            <mat-card class="estado-card">
              <mat-card-header>
                <mat-card-title>Balance General</mat-card-title>
                <mat-card-subtitle>{{ balanceGeneral.empresa }} - {{ balanceGeneral.periodo }}</mat-card-subtitle>
              </mat-card-header>
              <mat-card-content>
                <div class="balance-grid">
                  <!-- ACTIVOS -->
                  <div class="balance-section">
                    <h3 class="section-title activos">ACTIVOS</h3>

                    <div class="subsection" *ngIf="balanceGeneral.activosCorrientes.length > 0">
                      <h4>Activos Corrientes</h4>
                      <div class="linea-item" *ngFor="let activo of balanceGeneral.activosCorrientes">
                        <span class="cuenta-nombre">{{ activo.nombreCuenta }}</span>
                        <span class="cuenta-monto">{{ getMonto(activo) | currency:'GTQ':'symbol':'1.2-2' }}</span>
                      </div>
                      <div class="subtotal">
                        <span>Total Activos Corrientes</span>
                        <span>{{ getTotalActivosCorrientes() | currency:'GTQ':'symbol':'1.2-2' }}</span>
                      </div>
                    </div>

                    <div class="subsection" *ngIf="balanceGeneral.activosNoCorrientes.length > 0">
                      <h4>Activos No Corrientes</h4>
                      <div class="linea-item" *ngFor="let activo of balanceGeneral.activosNoCorrientes">
                        <span class="cuenta-nombre">{{ activo.nombreCuenta }}</span>
                        <span class="cuenta-monto">{{ getMonto(activo) | currency:'GTQ':'symbol':'1.2-2' }}</span>
                      </div>
                      <div class="subtotal">
                        <span>Total Activos No Corrientes</span>
                        <span>{{ getTotalActivosNoCorrientes() | currency:'GTQ':'symbol':'1.2-2' }}</span>
                      </div>
                    </div>

                    <div class="total-seccion activos">
                      <span>TOTAL ACTIVOS</span>
                      <span>{{ balanceGeneral.totalActivos | currency:'GTQ':'symbol':'1.2-2' }}</span>
                    </div>
                  </div>

                  <!-- PASIVOS Y PATRIMONIO -->
                  <div class="balance-section">
                    <h3 class="section-title pasivos">PASIVOS Y PATRIMONIO</h3>

                    <div class="subsection" *ngIf="balanceGeneral.pasivosCorrientes.length > 0">
                      <h4>Pasivos Corrientes</h4>
                      <div class="linea-item" *ngFor="let pasivo of balanceGeneral.pasivosCorrientes">
                        <span class="cuenta-nombre">{{ pasivo.nombreCuenta }}</span>
                        <span class="cuenta-monto">{{ getMonto(pasivo) | currency:'GTQ':'symbol':'1.2-2' }}</span>
                      </div>
                      <div class="subtotal">
                        <span>Total Pasivos</span>
                        <span>{{ balanceGeneral.totalPasivos | currency:'GTQ':'symbol':'1.2-2' }}</span>
                      </div>
                    </div>

                    <div class="subsection" *ngIf="balanceGeneral.patrimonio.length > 0">
                      <h4>Patrimonio</h4>
                      <div class="linea-item" *ngFor="let patrimonio of balanceGeneral.patrimonio">
                        <span class="cuenta-nombre">{{ patrimonio.nombreCuenta }}</span>
                        <span class="cuenta-monto">{{ getMonto(patrimonio) | currency:'GTQ':'symbol':'1.2-2' }}</span>
                      </div>
                      <div class="subtotal">
                        <span>Total Patrimonio</span>
                        <span>{{ balanceGeneral.totalPatrimonio | currency:'GTQ':'symbol':'1.2-2' }}</span>
                      </div>
                    </div>

                    <div class="total-seccion pasivos">
                      <span>TOTAL PASIVOS + PATRIMONIO</span>
                      <span>{{ (balanceGeneral.totalPasivos + balanceGeneral.totalPatrimonio) | currency:'GTQ':'symbol':'1.2-2' }}</span>
                    </div>
                  </div>
                </div>

                <!-- Verificación del balance -->
                <div class="balance-verification" [ngClass]="{'balanced': balanceGeneral.balanceado, 'unbalanced': !balanceGeneral.balanceado}">
                  <mat-icon>{{ balanceGeneral.balanceado ? 'check_circle' : 'error' }}</mat-icon>
                  <span>{{ balanceGeneral.balanceado ? 'Balance General: CUADRADO' : 'Balance General: DESCUADRADO' }}</span>
                  <small>(Activos = Pasivos + Patrimonio)</small>
                  <div *ngIf="!balanceGeneral.balanceado" class="diferencia">
                    Diferencia: {{ balanceGeneral.diferencia | currency:'GTQ':'symbol':'1.2-2' }}
                  </div>
                </div>
              </mat-card-content>
            </mat-card>
          </div>
        </mat-tab>

        <!-- Estado de Resultados -->
        <mat-tab label="Estado de Resultados">
          <div class="tab-content" *ngIf="estadoResultados">
            <mat-card class="estado-card">
              <mat-card-header>
                <mat-card-title>Estado de Resultados</mat-card-title>
                <mat-card-subtitle>{{ estadoResultados.empresa }} - {{ estadoResultados.periodo }}</mat-card-subtitle>
              </mat-card-header>
              <mat-card-content>
                <div class="resultados-container">

                  <!-- INGRESOS -->
                  <div class="resultado-section" *ngIf="estadoResultados.ingresos.length > 0">
                    <h3 class="section-title ingresos">INGRESOS</h3>
                    <div class="linea-item" *ngFor="let ingreso of estadoResultados.ingresos">
                      <span class="cuenta-nombre">{{ ingreso.nombreCuenta }}</span>
                      <span class="cuenta-monto">{{ getMonto(ingreso) | currency:'GTQ':'symbol':'1.2-2' }}</span>
                    </div>
                    <div class="subtotal ingresos">
                      <span>Total Ingresos</span>
                      <span>{{ estadoResultados.totalIngresos | currency:'GTQ':'symbol':'1.2-2' }}</span>
                    </div>
                  </div>

                  <!-- COSTOS -->
                  <div class="resultado-section" *ngIf="estadoResultados.costos && estadoResultados.costos.length > 0">
                    <h3 class="section-title costos">COSTOS</h3>
                    <div class="linea-item" *ngFor="let costo of estadoResultados.costos">
                      <span class="cuenta-nombre">{{ costo.nombreCuenta }}</span>
                      <span class="cuenta-monto">{{ getMonto(costo) | currency:'GTQ':'symbol':'1.2-2' }}</span>
                    </div>
                    <div class="subtotal costos">
                      <span>Total Costos</span>
                      <span>{{ estadoResultados.totalCostos | currency:'GTQ':'symbol':'1.2-2' }}</span>
                    </div>
                  </div>

                  <!-- GASTOS -->
                  <div class="resultado-section" *ngIf="estadoResultados.gastos.length > 0">
                    <h3 class="section-title gastos">GASTOS</h3>
                    <div class="linea-item" *ngFor="let gasto of estadoResultados.gastos">
                      <span class="cuenta-nombre">{{ gasto.nombreCuenta }}</span>
                      <span class="cuenta-monto">{{ getMonto(gasto) | currency:'GTQ':'symbol':'1.2-2' }}</span>
                    </div>
                    <div class="subtotal gastos">
                      <span>Total Gastos</span>
                      <span>{{ estadoResultados.totalGastos | currency:'GTQ':'symbol':'1.2-2' }}</span>
                    </div>
                  </div>

                  <!-- RESULTADO NETO -->
                  <div class="resultado-final" [ngClass]="{'utilidad': estadoResultados.utilidadNeta >= 0, 'perdida': estadoResultados.utilidadNeta < 0}">
                    <div class="calculo-resultado">
                      <div class="calculo-linea">
                        <span>Total Ingresos:</span>
                        <span>{{ estadoResultados.totalIngresos | currency:'GTQ':'symbol':'1.2-2' }}</span>
                      </div>
                      <div class="calculo-linea" *ngIf="estadoResultados.totalCostos > 0">
                        <span>(-) Total Costos:</span>
                        <span>({{ estadoResultados.totalCostos | currency:'GTQ':'symbol':'1.2-2' }})</span>
                      </div>
                      <div class="calculo-linea">
                        <span>(-) Total Gastos:</span>
                        <span>({{ estadoResultados.totalGastos | currency:'GTQ':'symbol':'1.2-2' }})</span>
                      </div>
                      <hr>
                      <div class="resultado-neto">
                        <span>{{ estadoResultados.tipoResultado === 'UTILIDAD' ? 'UTILIDAD NETA' : 'PÉRDIDA NETA' }}:</span>
                        <span class="monto-resultado">{{ estadoResultados.utilidadNeta | currency:'GTQ':'symbol':'1.2-2' }}</span>
                      </div>
                    </div>
                  </div>
                </div>
              </mat-card-content>
            </mat-card>
          </div>
        </mat-tab>
      </mat-tab-group>
    </div>
  `,
  styles: [`
    .page-container {
      padding: 20px;
      max-width: 1400px;
      margin: 0 auto;
    }

    .page-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 20px;
    }

    .page-header h1 {
      margin: 0;
      color: #4caf50;
    }

    .header-actions {
      display: flex;
      gap: 12px;
    }

    .loading-container, .error-container {
      display: flex;
      flex-direction: column;
      align-items: center;
      justify-content: center;
      padding: 40px;
      text-align: center;
    }

    .error-container mat-icon {
      font-size: 48px;
      width: 48px;
      height: 48px;
      margin-bottom: 16px;
    }

    .tab-content {
      padding: 20px 0;
    }

    .estado-card {
      margin-bottom: 20px;
    }

    .estado-card .mat-mdc-card-header {
      background: linear-gradient(135deg, #4caf50 0%, #66bb6a 100%);
      color: white;
      margin: -24px -24px 24px -24px;
      padding: 20px 24px;
      border-radius: 4px 4px 0 0;
    }

    /* Balance General */
    .balance-grid {
      display: grid;
      grid-template-columns: 1fr 1fr;
      gap: 40px;
      margin-bottom: 20px;
    }

    .balance-section {
      border: 1px solid #e0e0e0;
      border-radius: 8px;
      padding: 20px;
    }

    .section-title {
      margin: 0 0 20px 0;
      padding-bottom: 10px;
      border-bottom: 2px solid;
      font-size: 18px;
      font-weight: 600;
    }

    .section-title.activos {
      color: #2196f3;
      border-color: #2196f3;
    }

    .section-title.pasivos {
      color: #ff9800;
      border-color: #ff9800;
    }

    .section-title.ingresos {
      color: #4caf50;
      border-color: #4caf50;
    }

    .section-title.gastos, .section-title.costos {
      color: #f44336;
      border-color: #f44336;
    }

    .subsection {
      margin-bottom: 20px;
    }

    .subsection h4 {
      margin: 0 0 10px 0;
      color: #666;
      font-size: 14px;
      font-weight: 500;
      text-transform: uppercase;
    }

    .linea-item {
      display: flex;
      justify-content: space-between;
      padding: 6px 0;
      border-bottom: 1px dotted #ddd;
    }

    .cuenta-nombre {
      flex: 1;
      font-size: 14px;
    }

    .cuenta-monto {
      font-family: 'Courier New', monospace;
      font-weight: 500;
      font-size: 14px;
    }

    .subtotal {
      display: flex;
      justify-content: space-between;
      padding: 10px;
      margin-top: 10px;
      border-top: 1px solid #ccc;
      font-weight: 500;
      background-color: #f9f9f9;
    }

    .subtotal.ingresos {
      background-color: #e8f5e8;
      color: #2e7d32;
    }

    .subtotal.gastos, .subtotal.costos {
      background-color: #ffebee;
      color: #d32f2f;
    }

    .total-seccion {
      display: flex;
      justify-content: space-between;
      padding: 15px 10px;
      margin-top: 10px;
      border: 2px solid;
      border-radius: 4px;
      font-weight: bold;
      font-size: 16px;
    }

    .total-seccion.activos {
      background-color: #e3f2fd;
      border-color: #2196f3;
      color: #1976d2;
    }

    .total-seccion.pasivos {
      background-color: #fff3e0;
      border-color: #ff9800;
      color: #f57c00;
    }

    /* Estado de Resultados */
    .resultados-container {
      max-width: 600px;
      margin: 0 auto;
    }

    .resultado-section {
      margin-bottom: 30px;
      border: 1px solid #e0e0e0;
      border-radius: 8px;
      padding: 20px;
    }

    .resultado-final {
      border: 2px solid;
      border-radius: 8px;
      padding: 20px;
      margin-top: 20px;
    }

    .resultado-final.utilidad {
      background-color: #e8f5e8;
      border-color: #4caf50;
    }

    .resultado-final.perdida {
      background-color: #ffebee;
      border-color: #f44336;
    }

    .calculo-resultado {
      text-align: center;
    }

    .calculo-linea {
      display: flex;
      justify-content: space-between;
      margin: 10px 0;
      font-size: 16px;
    }

    .resultado-neto {
      display: flex;
      justify-content: space-between;
      font-size: 20px;
      font-weight: bold;
      margin-top: 15px;
    }

    .monto-resultado {
      font-family: 'Courier New', monospace;
    }

    /* Verificación del balance */
    .balance-verification {
      display: flex;
      align-items: center;
      justify-content: center;
      gap: 10px;
      padding: 15px;
      border-radius: 8px;
      margin-top: 20px;
      font-weight: 500;
      flex-wrap: wrap;
    }

    .balance-verification.balanced {
      background-color: #e8f5e8;
      color: #2e7d32;
    }

    .balance-verification.unbalanced {
      background-color: #ffebee;
      color: #d32f2f;
    }

    .balance-verification small {
      margin-left: 10px;
      font-style: italic;
      opacity: 0.8;
    }

    .diferencia {
      margin-top: 5px;
      font-size: 14px;
      font-weight: bold;
    }

    /* Responsivo */
    @media (max-width: 768px) {
      .page-container {
        padding: 16px;
      }

      .page-header {
        flex-direction: column;
        gap: 16px;
        align-items: stretch;
      }

      .header-actions {
        justify-content: space-between;
      }

      .balance-grid {
        grid-template-columns: 1fr;
        gap: 20px;
      }

      .resultados-container {
        max-width: 100%;
      }

      .calculo-linea {
        font-size: 14px;
      }

      .resultado-neto {
        font-size: 18px;
      }

      .balance-verification {
        flex-direction: column;
        text-align: center;
      }
    }
  `]
})
export class EstadosFinancierosComponent implements OnInit {
  balanceGeneral: BalanceGeneralDTO | null = null;
  estadoResultados: EstadoResultadosDTO | null = null;
  cargando = false;
  error: string | null = null;

  constructor(
    private estadosFinancierosService: EstadosFinancierosService,
    private snackBar: MatSnackBar
  ) {}

  ngOnInit(): void {
    this.generarEstados();
  }

  generarEstados(): void {
    this.cargando = true;
    this.error = null;

    // Cargar Balance General y Estado de Resultados en paralelo
    Promise.all([
      this.estadosFinancierosService.generarBalanceGeneral().toPromise(),
      this.estadosFinancierosService.generarEstadoResultadosAnual().toPromise()
    ]).then(([balance, resultados]) => {
      this.balanceGeneral = balance!;
      this.estadoResultados = resultados!;
      this.cargando = false;
      this.snackBar.open('Estados financieros generados exitosamente', 'Cerrar', { duration: 3000 });
    }).catch(error => {
      this.error = 'Error al generar los estados financieros. Por favor, inténtelo de nuevo.';
      this.cargando = false;
      console.error('Error:', error);
    });
  }

  exportarPDF(): void {
    this.snackBar.open('Función de exportar PDF próximamente', 'Cerrar', { duration: 3000 });
  }

  // Métodos auxiliares
  getMonto(linea: LineaEstadoDTO): number {
    return linea.monto || linea.saldoDeudor + linea.saldoAcreedor;
  }

  getTotalActivosCorrientes(): number {
    if (!this.balanceGeneral) return 0;
    return this.balanceGeneral.activosCorrientes.reduce((total, activo) => total + this.getMonto(activo), 0);
  }

  getTotalActivosNoCorrientes(): number {
    if (!this.balanceGeneral) return 0;
    return this.balanceGeneral.activosNoCorrientes.reduce((total, activo) => total + this.getMonto(activo), 0);
  }
}
