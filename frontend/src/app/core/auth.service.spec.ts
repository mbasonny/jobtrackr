import { TestBed } from '@angular/core/testing';

import { AuthService } from './auth.service';
import {HttpTestingController, provideHttpClientTesting} from '@angular/common/http/testing';
import {provideHttpClient} from '@angular/common/http';
import {environment} from '../../environments/environment';

describe('AuthService', () => {
  let service: AuthService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    localStorage.clear();

    TestBed.configureTestingModule({
      imports: [],
      providers: [
        AuthService,
        provideHttpClient(),
        provideHttpClientTesting()
      ]
    });
    service = TestBed.inject(AuthService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(( ) => httpMock.verify());

  it('devrait stocker le token et le courriel après une connexion réussie', () => {
    service.login({email: 'sonnytest21@gmail.com', password: 'secret123'}).subscribe();

    const req = httpMock.expectOne(`${environment.apiUrl}/auth/login`);
    req.flush({token: 'fake-jwt', email: 'sonnytest21@gmail.com', fullName: 'Sonny'});

    expect(service.getToken()).toBe('fake-jwt');
    expect(service.isAuthenticated()).toBeTrue();
    expect(service.currentUserEmail()).toBe('sonnytest21@gmail.com');
  });

  it('devrait effacer la session à la deconnexion', () => {
    localStorage.setItem('jobtrackr_token', 'fake-jwt');
    service.logout();
    expect(service.isAuthenticated()).toBeFalse();
  })
});
