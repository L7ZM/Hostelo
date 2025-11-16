import { TestBed } from '@angular/core/testing';

import { ServicesManageService } from './services-manage.service';

describe('ServicesManageService', () => {
  let service: ServicesManageService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ServicesManageService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
