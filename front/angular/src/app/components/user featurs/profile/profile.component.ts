import { CustomerDTO } from './../../../models/customer/customer-dto';
import { CustomerService } from '../../../services/customer/customer.service';
import { Component, OnInit } from '@angular/core';
import { AuthenticationService } from 'src/app/services/authentication/authentication.service';
import { MessageService } from 'primeng/api';
import { Router } from '@angular/router';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.scss'],
})
export class ProfileComponent implements OnInit {
  userProfile: CustomerDTO = {} as CustomerDTO; // Ensure this matches CustomerDTO structure
  newPassword = '';
  confirmPassword = '';
  passwordsMismatch = false;

  constructor(
    private messageService :MessageService ,
    private updateUserService: CustomerService,
    private authService: AuthenticationService,
    private router : Router
  ) {}

  ngOnInit(): void {

    this.fetchUserProfile();
  }

  fetchUserProfile(): void {
    const storedUser = localStorage.getItem('user');
    if (storedUser) {
      const parsedUser = JSON.parse(storedUser);
      if (parsedUser) {
        this.userProfile = parsedUser as CustomerDTO; // Cast to CustomerDTO
      }
    }
  }
  checkPasswords() {
    this.passwordsMismatch = this.newPassword !== this.confirmPassword;
  }




  update(): void {
    this.checkPasswords();
    if (this.passwordsMismatch) {
      return; // Exit if passwords don't match
    }

    const token = this.authService.getToken(); // Get the current token
    if (token && this.userProfile) {
      // Include the new password if it's not empty
      if (this.newPassword) {
        this.userProfile.password = this.newPassword;
      }

      this.updateUserService.updateCurrentCustomer(this.userProfile, token).subscribe({
        next: () => {
          this.messageService.add({
            severity: 'success',
            summary: 'Profile Updated',
            detail: 'Your profile has been updated successfully!',
          });

          // Clear the local storage and navigate to login
          localStorage.clear();
          setTimeout(() => {
            this.router.navigate(['/login']);
          }, 1000);
        },
        error: (error) => {
          console.error('Error updating profile:', error);
          this.messageService.add({
            severity: 'error',
            summary: 'Update Failed',
            detail: 'An error occurred while updating your profile. Please try again.',
          });
        },
      });
    } else {
      console.error('Token or user profile is missing');
      this.messageService.add({
        severity: 'warn',
        summary: 'Incomplete Data',
        detail: 'Token or user profile is missing. Please log in again.',
      });
    }
  }







}
