import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FormAsientoComponent } from './form-asiento.component';

describe('FormAsientoComponent', () => {
  let component: FormAsientoComponent;
  let fixture: ComponentFixture<FormAsientoComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [FormAsientoComponent]
    });
    fixture = TestBed.createComponent(FormAsientoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
