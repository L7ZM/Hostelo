import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { MenuItem } from 'primeng/api';
import { AuthenticationService } from 'src/app/services/authentication/authentication.service';
@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.scss'],
})
export class NavbarComponent implements OnInit {
  sidebarVisible: boolean = false;
  userRole: string = 'visitor'; // Default role
  profileItems: MenuItem[] = []; // Menu items
  isFidelityDialogVisible: boolean = false; // Controls dialog visibility
  fidelityPoints: number = 0; // This should come from your profile service or data
  constructor(
    private authService: AuthenticationService,
    private router: Router
  ) {}

  ngOnInit(): void {
    // Subscribe to role changes
    this.authService.userRole$.subscribe((role) => {
      this.userRole = role; // Update role dynamically
      this.updateProfileItems(); // Update menu items
      this.fetchUser(); // Fetch user profile
    });

    // Update the role based on the localStorage roles after refresh
    this.updateUserRole();
  }
  private fetchUser(): void {
    // Fetch user profile from localStorage
    const storedUser = localStorage.getItem('user');
    if (storedUser) {
      const parsedUser = JSON.parse(storedUser);
      if (parsedUser) {
        this.fidelityPoints = parsedUser.pointsFidelite || 0;  // Access fidelity points directly from the stored user
      }
    }
  }
  private updateUserRole(): void {
    const roles = this.authService.getRoles();
    if (roles.includes('ROLE_ADMIN')) {
      this.userRole = 'staff'; // If the user has the ADMIN role
    } else if (roles.includes('ROLE_USER')) {
      this.userRole = 'user'; // If the user has the USER role
    } else {
      this.userRole = 'visitor'; // Default for no roles
    }

    this.updateProfileItems();
  }

  private updateProfileItems(): void {
    if (this.userRole !== 'visitor') {
      this.profileItems = [
        {
          label: 'Edit Profile',
          icon: 'pi pi-user-edit',
          routerLink: '/profile',
        },
        {
          label: 'Fidelity Points',
          icon: 'pi pi-star',
          command: () => this.showFidelityDialog(),
        },
        {
          label: 'Logout',
          icon: 'pi pi-sign-out',
          command: () => this.logout(),
        },

      ];
    } else {
      this.profileItems = [
        { label: 'Login', icon: 'pi pi-sign-in', routerLink: '/login' },
      ];
    }
  }
  private showFidelityDialog(): void {
    this.isFidelityDialogVisible = true; // Open the dialog
  }

  logout(): void {
    this.authService.logout();
    this.router.navigate(['']); // Redirect to login after logout
  }

  // Check if the user is logged in
  isLoggedIn(): boolean {
    return !!this.authService.getToken() && !this.authService.isTokenExpired();
  }

  // Toggle sidebar visibility
  toggleSidebar(): void {
    this.sidebarVisible = !this.sidebarVisible;
  }
}
