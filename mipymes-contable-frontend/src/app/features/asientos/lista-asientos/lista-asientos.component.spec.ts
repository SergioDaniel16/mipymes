import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ListaAsientosComponent } from './lista-asientos.component';

describe('ListaAsientosComponent', () => {
  let component: ListaAsientosComponent;
  let fixture: ComponentFixture<ListaAsientosComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ListaAsientosComponent]
    });
    fixture = TestBed.createComponent(ListaAsientosComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
