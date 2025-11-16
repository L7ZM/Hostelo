import { data } from 'jquery';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class ReservationService {
  private ApiUrl = 'http://localhost:8080/api/reservations';

  constructor(private http: HttpClient) {}

  getAllReservation(): Observable<any> {
    return this.http.get<any>(this.ApiUrl);  }

  getMyReservation():Observable<any>{
    return this.http.get<any>(`${this.ApiUrl}/myBooking`);
  }

  cancelReservation(idReservation: number): Observable<void> {
    return this.http.delete<void>(`${this.ApiUrl}/${idReservation}`);
  }

  validateReservation(id: number): Observable<string> {
    return this.http.put<string>(`${this.ApiUrl}/${id}/validate`, {});
  }

  private getAuthToken(): string | null {
    return localStorage.getItem('token');
  }

  // Send POST request with Authorization header
  createReservation(reservationData: any): Observable<any> {
    const token = this.getAuthToken();

    // If token is available, add it to the header
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': token ? `Bearer ${token}` : ''
    });

    return this.http.post(this.ApiUrl, reservationData, { headers });
  }
  getFactureByReservationId(reservationId: number): Observable<any> {
    return this.http.get(`http://localhost:8080/api/factures/reservation/${reservationId}`);
  }
}
