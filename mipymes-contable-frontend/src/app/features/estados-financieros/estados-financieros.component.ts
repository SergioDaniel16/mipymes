// src/app/features/estados-financieros/estados-financieros.component.ts
import { Component, OnInit } from '@angular/core';

interface LineaEstado {
  codigo: string;
  nombreCuenta: string;
  monto: number;
}

@Component({
  selector: 'app-estados-financieros',
  template: `
    <div class="page-container">
      <div class="page-header">
        <h1>Estados Financieros</h1>
        <div class="header-actions">
          <button mat-raised-button color="primary" (click)="generarEstados()">
            <mat-icon>refresh</mat-icon>
            Actualizar
          </button>
          <button mat-stroked-button (click)="exportarPDF()">
            <mat-icon>picture_as_pdf</mat-icon>
            Exportar PDF
          </button>
        </div>
      </div>

      <!-- Pestañas para cambiar entre estados -->
      <mat-tab-group>
        <!-- Balance General -->
        <mat-tab label="Balance General">
          <div class="tab-content">
            <mat-card class="estado-card">
              <mat-card-header>
                <mat-card-title>Balance General</mat-card-title>
                <mat-card-subtitle>Almacén El Planeador - Al {{ fechaActual | date:'dd/MM/yyyy' }}</mat-card-subtitle>
              </mat-card-header>
              <mat-card-content>
                <div class="balance-grid">
                  <!-- ACTIVOS -->
                  <div class="balance-section">
                    <h3 class="section-title activos">ACTIVOS</h3>

                    <div class="subsection">
                      <h4>Activos Corrientes</h4>
                      <div class="linea-item" *ngFor="let activo of activosCorrientes">
                        <span class="cuenta-nombre">{{ activo.nombreCuenta }}</span>
                        <span class="cuenta-monto">{{ activo.monto | currency:'GTQ':'symbol':'1.2-2' }}</span>
                      </div>
                      <div class="subtotal">
                        <span>Total Activos Corrientes</span>
                        <span>{{ totalActivosCorrientes | currency:'GTQ':'symbol':'1.2-2' }}</span>
                      </div>
                    </div>

                    <div class="subsection">
                      <h4>Activos No Corrientes</h4>
                      <div class="linea-item" *ngFor="let activo of activosNoCorrientes">
                        <span class="cuenta-nombre">{{ activo.nombreCuenta }}</span>
                        <span class="cuenta-monto">{{ activo.monto | currency:'GTQ':'symbol':'1.2-2' }}</span>
                      </div>
                      <div class="subtotal">
                        <span>Total Activos No Corrientes</span>
                        <span>{{ totalActivosNoCorrientes | currency:'GTQ':'symbol':'1.2-2' }}</span>
                      </div>
                    </div>

                    <div class="total-seccion activos">
                      <span>TOTAL ACTIVOS</span>
                      <span>{{ totalActivos | currency:'GTQ':'symbol':'1.2-2' }}</span>
                    </div>
                  </div>

                  <!-- PASIVOS Y PATRIMONIO -->
                  <div class="balance-section">
                    <h3 class="section-title pasivos">PASIVOS Y PATRIMONIO</h3>

                    <div class="subsection">
                      <h4>Pasivos Corrientes</h4>
                      <div class="linea-item" *ngFor="let pasivo of pasivosCorrientes">
                        <span class="cuenta-nombre">{{ pasivo.nombreCuenta }}</span>
                        <span class="cuenta-monto">{{ pasivo.monto | currency:'GTQ':'symbol':'1.2-2' }}</span>
                      </div>
                      <div class="subtotal">
                        <span>Total Pasivos</span>
                        <span>{{ totalPasivos | currency:'GTQ':'symbol':'1.2-2' }}</span>
                      </div>
                    </div>

                    <div class="subsection">
                      <h4>Patrimonio</h4>
                      <div class="linea-item" *ngFor="let patrimonio of patrimonioItems">
                        <span class="cuenta-nombre">{{ patrimonio.nombreCuenta }}</span>
                        <span class="cuenta-monto">{{ patrimonio.monto | currency:'GTQ':'symbol':'1.2-2' }}</span>
                      </div>
                      <div class="subtotal">
                        <span>Total Patrimonio</span>
                        <span>{{ totalPatrimonio | currency:'GTQ':'symbol':'1.2-2' }}</span>
                      </div>
                    </div>

                    <div class="total-seccion pasivos">
                      <span>TOTAL PASIVOS + PATRIMONIO</span>
                      <span>{{ totalPasivos + totalPatrimonio | currency:'GTQ':'symbol':'1.2-2' }}</span>
                    </div>
                  </div>
                </div>

                <!-- Verificación del balance -->
                <div class="balance-verification" [ngClass]="{'balanced': estaBalanceado, 'unbalanced': !estaBalanceado}">
                  <mat-icon>{{ estaBalanceado ? 'check_circle' : 'error' }}</mat-icon>
                  <span>{{ estaBalanceado ? 'Balance General: CUADRADO' : 'Balance General: DESCUADRADO' }}</span>
                  <small>(Activos = Pasivos + Patrimonio)</small>
                </div>
              </mat-card-content>
            </mat-card>
          </div>
        </mat-tab>

        <!-- Estado de Resultados -->
        <mat-tab label="Estado de Resultados">
          <div class="tab-content">
            <mat-card class="estado-card">
              <mat-card-header>
                <mat-card-title>Estado de Resultados</mat-card-title>
                <mat-card-subtitle>Almacén El Planeador - Del 01/01/{{ anioActual }} al {{ fechaActual | date:'dd/MM/yyyy' }}</mat-card-subtitle>
              </mat-card-header>
              <mat-card-content>
                <div class="resultados-container">

                  <!-- INGRESOS -->
                  <div class="resultado-section">
                    <h3 class="section-title ingresos">INGRESOS</h3>
                    <div class="linea-item" *ngFor="let ingreso of ingresos">
                      <span class="cuenta-nombre">{{ ingreso.nombreCuenta }}</span>
                      <span class="cuenta-monto">{{ ingreso.monto | currency:'GTQ':'symbol':'1.2-2' }}</span>
                    </div>
                    <div class="subtotal ingresos">
                      <span>Total Ingresos</span>
                      <span>{{ totalIngresos | currency:'GTQ':'symbol':'1.2-2' }}</span>
                    </div>
                  </div>

                  <!-- GASTOS -->
                  <div class="resultado-section">
                    <h3 class="section-title gastos">GASTOS</h3>
                    <div class="linea-item" *ngFor="let gasto of gastos">
                      <span class="cuenta-nombre">{{ gasto.nombreCuenta }}</span>
                      <span class="cuenta-monto">{{ gasto.monto | currency:'GTQ':'symbol':'1.2-2' }}</span>
                    </div>
                    <div class="subtotal gastos">
                      <span>Total Gastos</span>
                      <span>{{ totalGastos | currency:'GTQ':'symbol':'1.2-2' }}</span>
                    </div>
                  </div>

                  <!-- RESULTADO NETO -->
                  <div class="resultado-final" [ngClass]="{'utilidad': utilidadNeta >= 0, 'perdida': utilidadNeta < 0}">
                    <div class="calculo-resultado">
                      <div class="calculo-linea">
                        <span>Total Ingresos:</span>
                        <span>{{ totalIngresos | currency:'GTQ':'symbol':'1.2-2' }}</span>
                      </div>
                      <div class="calculo-linea">
                        <span>(-) Total Gastos:</span>
                        <span>({{ totalGastos | currency:'GTQ':'symbol':'1.2-2' }})</span>
                      </div>
                      <hr>
                      <div class="resultado-neto">
                        <span>{{ utilidadNeta >= 0 ? 'UTILIDAD NETA' : 'PÉRDIDA NETA' }}:</span>
                        <span class="monto-resultado">{{ utilidadNeta | currency:'GTQ':'symbol':'1.2-2' }}</span>
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

    .section-title.gastos {
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
      padding: 10px 0;
      margin-top: 10px;
      border-top: 1px solid #ccc;
      font-weight: 500;
      background-color: #f9f9f9;
      padding-left: 10px;
      padding-right: 10px;
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

    .subtotal.ingresos {
      background-color: #e8f5e8;
      color: #2e7d32;
    }

    .subtotal.gastos {
      background-color: #ffebee;
      color: #d32f2f;
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
    }
  `]
})
export class EstadosFinancierosComponent implements OnInit {
  fechaActual = new Date();
  anioActual = new Date().getFullYear();

  // Balance General - Activos
  activosCorrientes: LineaEstado[] = [
    { codigo: '1001', nombreCuenta: 'Efectivo en Caja', monto: 38100 },
    { codigo: '1002', nombreCuenta: 'Bancos', monto: 68000 },
    { codigo: '1101', nombreCuenta: 'Inventario de Mercaderías', monto: 86000 }
  ];

  activosNoCorrientes: LineaEstado[] = [
    { codigo: '1201', nombreCuenta: 'Mobiliario y Equipo', monto: 29000 }
  ];

  // Balance General - Pasivos
  pasivosCorrientes: LineaEstado[] = [
    { codigo: '2001', nombreCuenta: 'Proveedores', monto: 45000 },
    { codigo: '2002', nombreCuenta: 'Letras de Cambio por Pagar', monto: 6000 }
  ];

  // Balance General - Patrimonio
  patrimonioItems: LineaEstado[] = [
    { codigo: '3001', nombreCuenta: 'Capital', monto: 165100 },
    { codigo: '3002', nombreCuenta: 'Utilidades del Ejercicio', monto: 5000 }
  ];

  // Estado de Resultados - Ingresos
  ingresos: LineaEstado[] = [
    { codigo: '4001', nombreCuenta: 'Ventas', monto: 44000 },
    { codigo: '4002', nombreCuenta: 'Otros Ingresos', monto: 1000 }
  ];

  // Estado de Resultados - Gastos
  gastos: LineaEstado[] = [
    { codigo: '5001', nombreCuenta: 'Alquiler', monto: 4000 },
    { codigo: '5002', nombreCuenta: 'Publicidad', monto: 1580 },
    { codigo: '5003', nombreCuenta: 'Sueldos', monto: 10400 },
    { codigo: '5101', nombreCuenta: 'Costo de Ventas', monto: 24020 }
  ];

  // Totales calculados
  totalActivosCorrientes = 0;
  totalActivosNoCorrientes = 0;
  totalActivos = 0;
  totalPasivos = 0;
  totalPatrimonio = 0;
  totalIngresos = 0;
  totalGastos = 0;
  utilidadNeta = 0;
  estaBalanceado = false;

  constructor() { }

  ngOnInit(): void {
    this.calcularTotales();
  }

  calcularTotales(): void {
    // Balance General
    this.totalActivosCorrientes = this.activosCorrientes.reduce((sum, item) => sum + item.monto, 0);
    this.totalActivosNoCorrientes = this.activosNoCorrientes.reduce((sum, item) => sum + item.monto, 0);
    this.totalActivos = this.totalActivosCorrientes + this.totalActivosNoCorrientes;

    this.totalPasivos = this.pasivosCorrientes.reduce((sum, item) => sum + item.monto, 0);
    this.totalPatrimonio = this.patrimonioItems.reduce((sum, item) => sum + item.monto, 0);

    this.estaBalanceado = Math.abs(this.totalActivos - (this.totalPasivos + this.totalPatrimonio)) < 0.01;

    // Estado de Resultados
    this.totalIngresos = this.ingresos.reduce((sum, item) => sum + item.monto, 0);
    this.totalGastos = this.gastos.reduce((sum, item) => sum + item.monto, 0);
    this.utilidadNeta = this.totalIngresos - this.totalGastos;
  }

  generarEstados(): void {
    console.log('Generando estados financieros actualizados...');
    this.calcularTotales();
  }

  exportarPDF(): void {
    console.log('Exportando estados financieros a PDF...');
  }
}
