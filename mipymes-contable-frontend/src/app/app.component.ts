// src/app/app.component.ts
import { Component, OnInit } from '@angular/core';
import { MatIconRegistry } from '@angular/material/icon';
import { DomSanitizer } from '@angular/platform-browser';

@Component({
  selector: 'app-root',
  template: `
    <router-outlet></router-outlet>
  `,
  styles: []
})
export class AppComponent implements OnInit {

  constructor(
    private matIconRegistry: MatIconRegistry,
    private domSanitizer: DomSanitizer
  ) {}

  ngOnInit(): void {
    // Registrar iconos personalizados si es necesario
    this.registerCustomIcons();
  }

  private registerCustomIcons(): void {
    // Aquí se pueden registrar iconos SVG personalizados
    // this.matIconRegistry.addSvgIcon(
    //   'custom-icon',
    //   this.domSanitizer.bypassSecurityTrustResourceUrl('assets/icons/custom-icon.svg')
    // );
  }
}
