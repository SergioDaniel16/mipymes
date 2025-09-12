import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HeaderComponent } from './layout/header/header.component';
import { SidebarComponent } from './layout/sidebar/sidebar.component';
import { MainLayoutComponent } from './layout/main-layout/main-layout.component';



@NgModule({
  declarations: [
    HeaderComponent,
    SidebarComponent,
    MainLayoutComponent
  ],
  imports: [
    CommonModule
  ]
})
export class CoreModule { }
