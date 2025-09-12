import { TestBed } from '@angular/core/testing';

import { ClientesProveedoresService } from './clientes-proveedores.service';

describe('ClientesProveedoresService', () => {
  let service: ClientesProveedoresService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ClientesProveedoresService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
