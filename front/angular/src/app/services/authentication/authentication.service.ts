import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { JwtHelperService } from '@auth0/angular-jwt';
import { BehaviorSubject, Observable, tap } from 'rxjs';
import { AuthenticationRequest } from 'src/app/models/auth/authentication-request';
import { AuthenticationResponse } from 'src/app/models/auth/authentication-response';
import { environment } from 'src/environments/environment';
import { CustomerRegistrationRequest } from 'src/app/models/customer/customer-registration-request';

@Injectable({
  providedIn: 'root',
})
export class AuthenticationService {

  private readonly authUrl = `${environment.api.baseUrl}/${environment.api.authUrl}`;
  private readonly registerUrl = `${environment.api.baseUrl}/${environment.api.registerUrl}`;
  private jwtHelper = new JwtHelperService();
  private userRoleSubject = new BehaviorSubject<string>('visitor'); // Initial role is 'visitor'
  userRole$ = this.userRoleSubject.asObservable(); // Observable to subscribe to role changes

  constructor(private http: HttpClient) {}

  login(authRequest: AuthenticationRequest): Observable<AuthenticationResponse> {
    return this.http.post<AuthenticationResponse>(this.authUrl, authRequest).pipe(
      tap((authenticationResponse: any) => {
        // Store the authentication response and token in local storage
        localStorage.setItem('user', JSON.stringify(authenticationResponse));

        // Extract roles and update user role state
        const roles = this.getRoles();
        this.updateUserRole(roles);
      })
    );
  }
  registerCustomer(customer: CustomerRegistrationRequest): Observable<void> {
    return this.http.post<void>(this.registerUrl, customer);
  }
  getToken(): string | null {
    const storedUser = localStorage.getItem('user');
    if (storedUser) {
      const authResponse: AuthenticationResponse = JSON.parse(storedUser);
      return authResponse.token; // Extract and return the token
    }
    return null;
  }

  isTokenExpired(): boolean {
    const token = this.getToken();
    return token ? this.jwtHelper.isTokenExpired(token) : true;
  }

  logout(): void {
    this.userRoleSubject.next('visitor');   // Reset role on logout
    localStorage.removeItem('user');

  }

  getRoles(): string[] {
    const storedUser = localStorage.getItem('user');
    if (storedUser) {
      const authResponse: AuthenticationResponse = JSON.parse(storedUser);
      return Array.isArray(authResponse.roles) ? authResponse.roles : [authResponse.roles];
    }
    return [];
  }

  // Update the user role based on roles in the local storage
  private updateUserRole(roles: string[]): void {
    if (roles.includes('ROLE_ADMIN')) {
      this.userRoleSubject.next('staff');
    } else if (roles.includes('ROLE_USER')) {
      this.userRoleSubject.next('user');
    } else {
      this.userRoleSubject.next('visitor');
    }
  }
}
