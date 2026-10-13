import { TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting, HttpTestingController } from '@angular/common/http/testing';
import { AuthService } from './auth.service';
import { environment } from '../../environments/environment'; // Ajustez le chemin selon votre projet

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

  afterEach(() => httpMock.verify());

  it('devrait stocker le token et le courriel après une connexion réussie', () => {
    // 1. On s'abonne à l'Observable pour déclencher la requête HTTP
    service.login({ email: 'sonnytest21@gmail.com', password: 'secret123' }).subscribe();

    // 2. On intercepte l'appel HTTP attendu
    const req = httpMock.expectOne(`${environment.apiUrl}/auth/login`);
    expect(req.request.method).toBe('POST'); // Optionnel mais recommandé : vérifie que c'est un POST

    // 3. On simule la réponse du serveur (le flush doit être fait AVANT les expects)
    req.flush({ token: 'fake-jwt', email: 'sonnytest21@gmail.com', fullName: 'Sonny' });

    // 4. On vérifie que le service a bien mis à jour son état interne et le localStorage
    expect(service.getToken()).toBe('fake-jwt');
    expect(service.isAuthenticated()).toBeTrue();
    expect(service.currentUserEmail()).toBe('sonnytest21@gmail.com');
  });

  it('devrait effacer la session à la deconnexion', () => {
    // ATTENTION : Assurez-vous que la clé 'jobtrackr_token' correspond bien à celle
    // utilisée à l'intérieur de la méthode service.getToken() / service.logout()
    localStorage.setItem('jobtrackr_token', 'fake-jwt');

    // Si votre service utilise aussi une variable d'état ou un Signal pour currentUserEmail,
    // l'appel à logout() va réinitialiser tout cela.
    service.logout();

    expect(service.isAuthenticated()).toBeFalse();
    expect(service.getToken()).toBeNull(); // Optionnel : vérifie que le token a bien disparu
  });
});
