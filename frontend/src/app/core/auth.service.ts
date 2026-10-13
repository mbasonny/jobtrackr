import {Injectable, signal} from '@angular/core';
import {AuthResponse, LoginPayload, RegisterPayload} from './model';
import {Observable, tap} from 'rxjs';
import {environment} from '../../environments/environment';
import {HttpClient} from '@angular/common/http';

const TOKEN_KEY = 'jobtrackr_token';
const USER_KEY = 'jobtrackr_user';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  readonly currentUserEmail = signal<string | null>(this.readStoredEmail())

  constructor(private http: HttpClient) { }

  register(payload: RegisterPayload): Observable<AuthResponse> {
    return this.http
      .post<AuthResponse>(`${environment.apiUrl}/auth/register`, payload)
      .pipe(tap((res) => this.persistSession(res)));
  }
  login(payload: LoginPayload): Observable<AuthResponse> {
    return this.http
      .post<AuthResponse>(`${environment.apiUrl}/auth/login`, payload)
      .pipe(tap((res) => this.persistSession(res)));
  }

  logout(): void{
    localStorage.removeItem(TOKEN_KEY);
    localStorage.removeItem(USER_KEY);
    this.currentUserEmail.set(null);
  }

  getToken(): string | null {
    return localStorage.getItem(TOKEN_KEY);
  }
  isAuthenticated(): boolean {
    return !!this.getToken()
  }

  private persistSession(res: AuthResponse): void {
    localStorage.setItem(TOKEN_KEY, res.token);
    localStorage.setItem(USER_KEY, res.email);
    this.currentUserEmail.set(res.email);
  }

  private readStoredEmail(): string | null {
    return localStorage.getItem(USER_KEY);
  }
}
