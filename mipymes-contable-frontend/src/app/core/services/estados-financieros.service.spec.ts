import { TestBed } from '@angular/core/testing';

import { EstadosFinancierosService } from './estados-financieros.service';

describe('EstadosFinancierosService', () => {
  let service: EstadosFinancierosService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(EstadosFinancierosService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
