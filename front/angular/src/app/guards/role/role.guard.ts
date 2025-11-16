import { Injectable } from '@angular/core';
import { CanActivate, ActivatedRouteSnapshot, Router, RouterStateSnapshot } from '@angular/router';
import { AuthenticationService } from '../../services/authentication/authentication.service';

@Injectable({
  providedIn: 'root',
})
export class RoleGuard implements CanActivate {
  constructor(
    private authService: AuthenticationService,
    private router: Router
  ) {}

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean {
    const requiredRoles: string[] = route.data['roles']; // Get required roles from route data
    const userRoles: string[] = this.authService.getRoles(); // Get user roles from auth service

    // If no roles are required, allow access
    if (!requiredRoles) {
      return true;
    }

    // If user has no roles, or roles don't match, deny access
    const hasRole = userRoles.some((role) => requiredRoles.includes(role));
    if (!hasRole) {
      this.router.navigate(['/']); // Redirect to home
      return false;
    }

    return true;
  }

}
