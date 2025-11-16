import { HttpClient } from '@angular/common/http';
import { Component, ViewEncapsulation } from '@angular/core';
import { ConfirmationService, MessageService } from 'primeng/api';
import { Reservation } from 'src/app/models/reservation/reservation.model';
import { ReservationService } from 'src/app/services/reservation/reservation.service';

@Component({
  selector: 'app-my-bookings',
  templateUrl: './my-bookings.component.html',
  styleUrls: ['./my-bookings.component.scss'],
  encapsulation: ViewEncapsulation.None,
})
export class MyBookingsComponent {
  bookings: Reservation[] = [];
  factureDialogVisible: boolean=false;
  facture: any;


  constructor(private http: HttpClient,
     private reservationService: ReservationService,
        private messageService: MessageService,
        private confirmationService: ConfirmationService,
        private factureService: ReservationService
  ) {}

  ngOnInit(): void {
    this.fetchBookings();
  }

  fetchBookings(): void {
    this.reservationService.getMyReservation().subscribe(
      (data) => (this.bookings = data),
      (error) => console.error('Error fetching bookings:', error)
    );
  }

  cancelReservation(booking: Reservation) {
    const now = new Date();
    const bookingDateDebut = new Date(booking.dateDebut);

    const diffInHours = Math.abs((bookingDateDebut.getTime() - now.getTime()) / (1000 * 60 * 60)); // Difference in hours

    if (diffInHours < 48) {
      this.confirmationService.confirm({
        message: `This reservation is less than 48 hours from now. Cancelling it will result in a deduction of fidelity points. Do you want to proceed?`,
        header: 'Fidelity Points Deduction',
        icon: 'pi pi-exclamation-triangle',
        accept: () => {
          this.performCancellation(booking.id);
        },
        reject: () => {
          this.messageService.add({
            severity: 'info',
            summary: 'Cancelled',
            detail: 'Reservation cancellation cancelled'
          });
        }
      });
    } else {
      this.confirmationService.confirm({
        message: 'Are you sure you want to cancel this reservation?',
        header: 'Confirm Cancellation',
        icon: 'pi pi-exclamation-triangle',
        accept: () => {
          this.performCancellation(booking.id);
        },
        reject: () => {
          this.messageService.add({
            severity: 'info',
            summary: 'Cancelled',
            detail: 'Reservation cancellation cancelled'
          });
        }
      });
    }
  }

  private performCancellation(bookingId: number) {
    this.reservationService.cancelReservation(bookingId).subscribe({
      next: () => {
        this.messageService.add({
          severity: 'success',
          summary: 'Success',
          detail: 'Reservation cancelled successfully'
        });
        this.fetchBookings();
      },
      error: () => {
        this.messageService.add({
          severity: 'info',
          summary: 'Info',
          detail: 'You cannot cancel the reservation within 48 hours of check-in'
        });
      }
    });
  }
  viewFacture(booking: any): void {
    this.factureDialogVisible = true;
    const reservationId = booking.id; // Adjust according to your data structure

    this.factureService.getFactureByReservationId(reservationId).subscribe(
      (data: any) => {
        this.facture = data;
      },
      (error) => {
        console.error('Error fetching facture:', error);
        this.factureDialogVisible = false;
      }
    );
  }

  clearFacture(): void {
    this.facture = null;
  }
}
