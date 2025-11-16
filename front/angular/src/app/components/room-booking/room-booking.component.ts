import { Component, HostListener, OnInit } from '@angular/core';
import { RoomService } from 'src/app/services/rooms/room-service.service';
import { BookingDialogComponent } from '../user featurs/booking-dialog/booking-dialog.component';
import { DialogService } from 'primeng/dynamicdialog';
import { MessageService } from 'primeng/api';
import { AuthenticationService } from 'src/app/services/authentication/authentication.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-room-booking',
  templateUrl: './room-booking.component.html',
  styleUrls: ['./room-booking.component.scss'],
})
export class RoomBookingComponent implements OnInit {
  dateFrom!: Date;
  dateTo!: Date;
  rooms: any[] = [];
  filteredRooms: any[] = [];
  navbarfixed: boolean = false;

  // Filter properties
  visible: boolean = false;
  priceRange: number[] = [100, 1000];
  roomTypes: string[] = ['SIMPLE', 'DELUXE', 'DOUBLE'];
  selectedRoomTypes: string[] = [];
  availabilityOptions: string[] = ['DISPONIBLE', 'OCCUPEE', 'EN_ENTRETIEN'];
  selectedAvailability: string = '';

  // Error flags
  dateFromError: boolean = false;
  dateToError: boolean = false;

  @HostListener('window:scroll', ['$event'])
  onscroll(): void {
    this.navbarfixed = window.scrollY > 100;
  }

  constructor(
    private roomService: RoomService,
    private dialogService: DialogService,
    private messageService: MessageService,
    private authService: AuthenticationService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.fetchRooms();
  }
  validateDates(): void {
    // Reset errors
    this.dateFromError = !this.dateFrom;
    this.dateToError = !this.dateTo;

    // Additional custom validation can go here if needed
  }
  // Fetch rooms from API
  fetchRooms(): void {
    this.roomService.getAll().subscribe(
      (data) => {
        this.rooms = data;
        this.filteredRooms = [...this.rooms];
      },
      (error) => {
        console.error('Error fetching room data', error);
      }
    );
  }

  // Apply filters to rooms
  applyFilters(): void {
    this.filteredRooms = this.rooms.filter((room) => {
      const inPriceRange =
        room.prix >= this.priceRange[0] && room.prix <= this.priceRange[1];
      const matchesType =
        this.selectedRoomTypes.length === 0 ||
        this.selectedRoomTypes.includes(room.type);
      const matchesAvailability =
        this.selectedAvailability === '' ||
        room.etat === this.selectedAvailability;

      return inPriceRange && matchesType && matchesAvailability;
    });
    this.visible = false; // Close the filter dialog
  }

  // Reset all filters
  resetFilters(): void {
    this.priceRange = [100, 1000];
    this.selectedRoomTypes = [];
    this.selectedAvailability = '';
    this.filteredRooms = [...this.rooms];
  }

  // Open booking dialog for a room
  openBookingDialog(room: any): void {
    // Check if the user is logged in using the token
    if (!this.authService.getToken()) {
      this.messageService.add({
        severity: 'warn',
        summary: 'Not Logged In',
        detail: 'You need to log in to make a booking.',
      });
      this.router.navigate(['/login']);
      return;
    }

    // Reset error flags before validation
    this.dateFromError = false;
    this.dateToError = false;

    // Validate date inputs
    const today = new Date();
    today.setHours(0, 0, 0, 0); // Ensure time is ignored by setting it to midnight

    if (!this.dateFrom || !this.dateTo) {
      if (!this.dateFrom) this.dateFromError = true;
      if (!this.dateTo) this.dateToError = true;

      this.messageService.add({
        severity: 'error',
        summary: 'Missing Dates',
        detail: 'Both start and end dates are required.',
      });
      return; // Prevent dialog from opening
    }

    // Check if the start date is in the past
    const startDate = new Date(this.dateFrom);
    if (startDate < today) {
      this.messageService.add({
        severity: 'error',
        summary: 'Invalid Start Date',
        detail: 'The start date cannot be in the past.',
      });
      this.dateFromError = true; // Highlight the invalid date
      return; // Prevent dialog from opening
    }

    // Proceed to open the dialog if validation passes
    const ref = this.dialogService.open(BookingDialogComponent, {
      data: {
        room: room,
        dateFrom: this.dateFrom,
        dateTo: this.dateTo,
      },
      header: 'Booking Details',
      width: '50%',
    });

    ref.onClose.subscribe(() => {
      // Handle post-close logic if needed
    });
  }
}
