import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { CustomerRegistrationRequest } from '../../models/customer/customer-registration-request';
import { CustomerService } from '../../services/customer/customer.service';
import { AuthenticationService } from '../../services/authentication/authentication.service';
import { AuthenticationRequest } from '../../models/auth/authentication-request';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss']
})
export class RegisterComponent {
  errorMsg = '';
  customer: CustomerRegistrationRequest = {};
  constructor(
    private router: Router,

    private authenticationService: AuthenticationService
  ) {
  }

  login() {
    this.router.navigate(['login']);
  }

  createAccount() {
    this.authenticationService.registerCustomer(this.customer)
      .subscribe({
        next: () => {
          // Create the AuthenticationRequest from registered customer details
          const authReq: AuthenticationRequest = {
            email: this.customer.email,
            password: this.customer.password
          };
          console.log('Auth Request:', authReq);

          // Immediately log in after registration
          this.authenticationService.login(authReq)
            .subscribe({
              next: (authenticationResponse: any) => {
                console.log('Authentication Response:', authenticationResponse);
                // Store the user in local storage
                localStorage.setItem('user', JSON.stringify(authenticationResponse));
                // Navigate to the home page
                this.router.navigate(['']);
              },
              error: (err: { error: { statusCode: number; }; }) => {
                console.error('Login Error:', err);
                if (err.error.statusCode === 401) {
                  this.errorMsg = 'Login and / or password is incorrect';
                } else {
                  this.errorMsg = 'Unexpected error occurred during login.';
                }
              }
            });
        },
        error: (err) => {
          console.error('Registration Error:', err);
          this.errorMsg = 'Registration failed. Please try again.';
        }
      });
  }

}
