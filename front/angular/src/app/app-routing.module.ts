

import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { CustomerComponent } from './components/admin features/customers Management/customer/customer.component';
import { LoginComponent } from './components/login/login.component';
import { RegisterComponent } from './components/register/register.component';
import { RoomManagementComponent } from './components/admin features/room-management/room-management.component';
import { HomeComponent } from './components/home/home.component';
import { VirtualTourComponent } from './components/virtual-tour/virtual-tour.component';
import { AboutComponent } from './components/about/about.component';
import { ContactComponent } from './components/contact/contact.component';
import { ProfileComponent } from './components/user featurs/profile/profile.component';
import { RoleGuard } from './guards/role/role.guard';
import { NoAuthGuard } from './guards/no auth/no-auth.guard';
import { ServicesManagementComponent } from './components/admin features/services-management/services-management.component';
import { RoomBookingComponent } from './components/room-booking/room-booking.component';
import { BookingManagementComponent } from './components/admin features/booking-management/booking-management.component';
import { MyBookingsComponent } from './components/user featurs/my-bookings/my-bookings.component';

const routes: Routes = [
  {
    path: '',

    component : HomeComponent
  },
  {
    path: 'profile',
    component: ProfileComponent  ,
    canActivate: [RoleGuard],
    data: { roles: ['ROLE_ADMIN', 'ROLE_USER'] }, // Accessible to both roles
  },
  {
    path: 'book',
    component: RoomBookingComponent  ,
    canActivate: [RoleGuard],
    data: { roles: ['ROLE_ADMIN', 'ROLE_USER'] }, // Accessible to both roles
  },
  {
    path: 'contact',
    component: ContactComponent
  },
  {
    path: 'about',
    component: AboutComponent
  },
  {
    path: 'tour',
    component: VirtualTourComponent
  },

  {
    path: 'login',
    component: LoginComponent,
    canActivate:[NoAuthGuard]
  },
  {
    path: 'register',
    component: RegisterComponent,
    canActivate:[NoAuthGuard]
  },
  {
    path: 'customers',
    component: CustomerComponent,
    canActivate: [RoleGuard],
    data: { roles: ['ROLE_ADMIN'] },
  },
  {
    path: 'rooms',
    component: RoomManagementComponent,
    canActivate: [RoleGuard],
    data: { roles: ['ROLE_ADMIN'] },
   },
   {
    path: 'services',
    component: ServicesManagementComponent,
    canActivate: [RoleGuard],
    data: { roles: ['ROLE_ADMIN'] },
   },
   {
    path: 'ManageBooking',
    component: BookingManagementComponent,
    canActivate: [RoleGuard],
    data: { roles: ['ROLE_ADMIN'] },
   },
   {
    path: 'myBookings',
    component: MyBookingsComponent ,
    canActivate: [RoleGuard],
    data: { roles: ['ROLE_ADMIN', 'ROLE_USER'] },
  },


];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
