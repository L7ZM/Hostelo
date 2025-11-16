import { Reservation } from 'src/app/models/reservation/reservation.model';
import { Component, OnInit, Inject } from '@angular/core';
import { DynamicDialogRef, DynamicDialogConfig } from 'primeng/dynamicdialog';
import { ServicesManageService } from 'src/app/services/servicesManagement/services-manage.service';
import { ReservationService } from 'src/app/services/reservation/reservation.service';
import { ConfirmationService, MessageService } from 'primeng/api';
import { Router } from '@angular/router';

@Component({
  selector: 'app-booking-dialog',
  templateUrl: './booking-dialog.component.html',
  styleUrls: ['./booking-dialog.component.scss'],
})
export class BookingDialogComponent implements OnInit {
  room: any; // The room data
  dateFrom!: Date; // Start date passed from RoomBookingComponent
  dateTo!: Date;
  services: any[] = []; // Available services
  selectedServices: any[] = []; // Selected services
  totalPrice: number = 0; // Total price for the booking
  isVisible: boolean = false; // Dialog visibility
  selectedServicesMap: Map<string, boolean> = new Map();
  totalDays!: number;
  readonly requiredServiceName: string[] = ['Breakfast', 'petit dejeuner'];
  finalPrice: number = 0; // Final price after discount (calculated separately)
  fidelityPoints: number = 0; // Fidelity points from user profile
  applyPoints: boolean = false; // Whether to apply fidelity points


  constructor(
    private router: Router,
    private messageService: MessageService, // Inject the MessageService
    private confirmationService: ConfirmationService,
    private reservationService: ReservationService,
    private servicesService: ServicesManageService,
    private ref: DynamicDialogRef,
    @Inject(DynamicDialogConfig) private config: DynamicDialogConfig
  ) {}

  ngOnInit(): void {
    // Initialize room data
    const { room, dateFrom, dateTo } = this.config.data;
    this.room = room;
    this.dateFrom = new Date(dateFrom); // Ensure the dates are Date objects
    this.dateTo = new Date(dateTo);
    this.fetchUser(); // Fetch user profile from localStorage

    this.fetchServices();
    this.calculateTotalDays(); // Calculate the days on init
    this.calculateFinalPrice()
  }
  private fetchUser(): void {
    // Fetch user profile from localStorage
    const storedUser = localStorage.getItem('user');
    if (storedUser) {
      const parsedUser = JSON.parse(storedUser);
      if (parsedUser) {
        this.fidelityPoints = parsedUser.pointsFidelite; // Access fidelity points directly from the stored user
      }
    }
  }
  fetchServices(): void {
    this.servicesService.getAllServices().subscribe(
      (data) => {
        this.services = data.map((service: any) => {
          const isRequired = this.requiredServiceName.includes(
            service.nomService
          );
          if (isRequired) {
            this.selectedServicesMap.set(service.nomService, true); // Mark required service as checked
            this.selectedServices.push(service); // Automatically add to selected services
          } else {
            this.selectedServicesMap.set(service.nomService, false); // Ensure non-required services are unchecked
          }
          return { ...service, isRequired };
        });

        this.calculateTotalDays();
        this.totalPrice =
          this.room.prix * this.totalDays +
          this.selectedServices.reduce((sum, s) => sum + s.prix, 0);
      },
      (error) => {
        console.error('Error fetching services', error);
      }
    );
  }

  calculateTotalDays(): void {
    if (this.dateFrom && this.dateTo) {
      // Get the time difference in milliseconds
      const timeDifference = this.dateTo.getTime() - this.dateFrom.getTime();

      // If the time difference is negative, set days to 0 or handle as needed
      if (timeDifference < 0) {
        console.error('End date must be after the start date');
        this.totalDays = 0;
      } else {
        // Calculate the number of days
        this.totalDays = Math.ceil(timeDifference / (1000 * 3600 * 24)); // Round up to account for partial days
      }
    } else {
      console.error('Invalid dates');
      this.totalDays = 0;
    }
  }

  updatePrice(service: any): void {
    // Get current state
    const wasSelected = this.selectedServicesMap.get(service.nomService);

    // Update state to new value (opposite of what it was)
    this.selectedServicesMap.set(service.nomService, !wasSelected);

    // Update selected services list
    if (!wasSelected) {
      this.selectedServices.push(service);
    } else {
      this.selectedServices = this.selectedServices.filter(
        (s) => s.nomService !== service.nomService
      );
    }


    // Recalculate total price after selecting a service
    this.calculateTotalDays(); // Ensure days are recalculated
    if (this.totalDays > 0) {
      // Calculate total price including room price and selected services
      this.totalPrice =
        this.room.prix * this.totalDays + // Multiply room price by the number of days
        this.selectedServices.reduce((sum, s) => sum + s.prix, 0); // Add price of selected services
    }

    this.calculateFinalPrice()
  }
  calculateFinalPrice() {
    console.log('Apply Points:', this.applyPoints); // Debugging log

    // If applyPoints is true (checkbox is checked), increase the final price by 50% of the total price
    if (this.applyPoints) {
      this.finalPrice = this.totalPrice * 0.5; // Add 50% to total price
    } else {
      // If unchecked, return the total price
      this.finalPrice = this.totalPrice;
    }
  }
  onApplyPointsChange(event: Event): void {
    this.applyPoints = (event.target as HTMLInputElement).checked;
    this.calculateFinalPrice();
  }
  closeDialog() {
    this.ref.close();
  }

  // The reserveRoom method now uses ReservationService to submit the reservation
  reserveRoom() {
    // Prepare reservation data
    const reservationData = {
      chambreId: this.room.id, // Assume room has an 'id' property
      dateDebut: this.dateFrom.toISOString().split('T')[0], // Convert to YYYY-MM-DD
      dateFin: this.dateTo.toISOString().split('T')[0], // Convert to YYYY-MM-DD
      serviceIds: this.selectedServices.map((service) => service.id), // Assuming each service has an 'id' property
      usePoints : this.applyPoints
    };

    // Call the ReservationService to create the reservation
    this.reservationService.createReservation(reservationData).subscribe(
      (response) => {
        // Show success message
        this.messageService.add({
          severity: 'success',
          summary: 'Reservation Successful',
          detail: 'Your room has been successfully reserved.'
        });
        // Optionally close the dialog
        this.closeDialog();
      },
      (error) => {
        console.error('Error making reservation', error);

        // Show error message
        this.messageService.add({
          severity: 'error',
          summary: 'Reservation Failed',
          detail:
            'There was an error making your reservation. Please try again.',
        });
      }
    );
  }
}
