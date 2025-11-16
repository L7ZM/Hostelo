import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { InputTextModule } from 'primeng/inputtext';
import { CustomerComponent } from './components/admin features/customers Management/customer/customer.component';
import { AvatarModule } from 'primeng/avatar';
import { ButtonModule } from 'primeng/button';
import { RippleModule } from 'primeng/ripple';
import { MenuModule } from 'primeng/menu';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { SidebarModule } from 'primeng/sidebar';
import { ManageCustomerComponent } from './components/admin features/customers Management/manage-customer/manage-customer.component';
import { LoginComponent } from './components/login/login.component';
import { HTTP_INTERCEPTORS, HttpClientModule } from '@angular/common/http';
import { MessageModule } from 'primeng/message';
import { HttpInterceptorService } from './services/interceptor/http-interceptor.service';
import { CustomerCardComponent } from './components/admin features/customers Management/customer-card/customer-card.component';
import { TableModule } from 'primeng/table';
import { ToolbarModule } from 'primeng/toolbar';
import { DialogModule } from 'primeng/dialog';
import { InputNumberModule } from 'primeng/inputnumber';
import { DropdownModule } from 'primeng/dropdown';
import { FileUploadModule } from 'primeng/fileupload';
import { InputTextareaModule } from 'primeng/inputtextarea';
import { CardModule } from 'primeng/card';
import { BadgeModule } from 'primeng/badge';
import { ToastModule } from 'primeng/toast';
import { CalendarModule } from 'primeng/calendar';
import { ListboxModule } from 'primeng/listbox';
import { CheckboxModule } from 'primeng/checkbox';
import { CarouselModule } from 'primeng/carousel';
import { DividerModule } from 'primeng/divider';
import { ConfirmationService, MessageService } from 'primeng/api';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { SplitterModule } from 'primeng/splitter';
import { SliderModule } from 'primeng/slider';
import { TooltipModule } from 'primeng/tooltip';
import { MultiSelectModule } from 'primeng/multiselect';
import { RadioButtonModule } from 'primeng/radiobutton';
import { RegisterComponent } from './components/register/register.component';
import { RoomManagementComponent } from './components/admin features/room-management/room-management.component';
import { HomeComponent } from './components/home/home.component';
import { VirtualTourComponent } from './components/virtual-tour/virtual-tour.component';
import { AboutComponent } from './components/about/about.component';
import { ContactComponent } from './components/contact/contact.component';
import { ProfileComponent } from './components/user featurs/profile/profile.component';
import { NavbarComponent } from './components/shared/navbar/navbar.component';
import { FooterComponent } from './components/shared/footer/footer.component';
import { ServicesManagementComponent } from './components/admin features/services-management/services-management.component';
import { BookingManagementComponent } from './components/admin features/booking-management/booking-management.component';
import { RoomBookingComponent } from './components/room-booking/room-booking.component';
import { BookingDialogComponent } from './components/user featurs/booking-dialog/booking-dialog.component';
import { DialogService } from 'primeng/dynamicdialog';
import { TagModule } from 'primeng/tag';
import { GalleriaModule } from 'primeng/galleria';
import { MyBookingsComponent } from './components/user featurs/my-bookings/my-bookings.component';


@NgModule({
  declarations: [
    NavbarComponent,
    FooterComponent,
    AppComponent,
    CustomerComponent,
    ManageCustomerComponent,
    LoginComponent,
    CustomerCardComponent,
    RegisterComponent,
    RoomManagementComponent,
    HomeComponent,
    VirtualTourComponent,
    AboutComponent,
    ContactComponent,
    ProfileComponent,
    ServicesManagementComponent,
    BookingManagementComponent,
    RoomBookingComponent,
    BookingDialogComponent
,MyBookingsComponent,
  ],

  imports: [
    TagModule ,
    RadioButtonModule,
    TooltipModule,
    SliderModule,
    MultiSelectModule,
    GalleriaModule ,
    SplitterModule,
    DividerModule,
    ReactiveFormsModule,
    CheckboxModule,
    ListboxModule,
    CalendarModule,
    FormsModule,
    CarouselModule,
    InputTextareaModule,
    FileUploadModule,
    DropdownModule,
    InputNumberModule,
    DialogModule,
    ToolbarModule,
    TableModule,
    BrowserModule,
    BrowserAnimationsModule,
    AppRoutingModule,
    FormsModule,
    InputTextModule,
    AvatarModule,
    ButtonModule,
    RippleModule,
    MenuModule,
    SidebarModule,
    HttpClientModule,
    MessageModule,
    CardModule,
    BadgeModule,
    ToastModule,
    ConfirmDialogModule,
    FormsModule,
    SliderModule, // Add FormsModule for ngModel binding
    SidebarModule,
  ],
  providers: [
    {
      provide: HTTP_INTERCEPTORS,
      useClass: HttpInterceptorService,
      multi: true,
    },
    MessageService,
    ConfirmationService,
    DialogService,
  ],
  bootstrap: [AppComponent],
})
export class AppModule {}
