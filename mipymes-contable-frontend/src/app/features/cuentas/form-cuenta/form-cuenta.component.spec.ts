import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FormCuentaComponent } from './form-cuenta.component';

describe('FormCuentaComponent', () => {
  let component: FormCuentaComponent;
  let fixture: ComponentFixture<FormCuentaComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [FormCuentaComponent]
    });
    fixture = TestBed.createComponent(FormCuentaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
