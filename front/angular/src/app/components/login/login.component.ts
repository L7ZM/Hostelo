import { Component } from '@angular/core';
import { AuthenticationRequest } from '../../models/auth/authentication-request';
import { AuthenticationService } from '../../services/authentication/authentication.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent {
  authenticationRequest: AuthenticationRequest = { email: '', password: '' };  // Set default structure
  errorMsg = '';
  isLoading = false;  // For showing a loading spinner

  constructor(
    private authenticationService: AuthenticationService,
    private router: Router
  ) {}

  login() {
    this.errorMsg = '';
    this.isLoading = true;  // Start loading spinner
    this.authenticationService.login(this.authenticationRequest)
      .subscribe({
        next: (authenticationResponse) => {
          localStorage.setItem('user', JSON.stringify(authenticationResponse));
          this.isLoading = false;  // Stop loading spinner

          this.router.navigate(['']);  // Redirect to home page or based on user role
        },
        error: (err) => {
          this.isLoading = false;  // Stop loading spinner
          if (err.error.statusCode === 401) {
            this.errorMsg = 'Login and/or password is incorrect';
          } else {
            this.errorMsg = 'Login and/or password is incorrect';
          }
        }
      });
  }

  register() {
    this.router.navigate(['register']);
  }
}
