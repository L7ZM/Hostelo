import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse, HttpHeaders } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { Room } from 'src/app/models/room/room.model';

@Injectable({
  providedIn: 'root'
})
export class RoomService {
  private baseUrl = 'http://localhost:8080/api/chambres';

  constructor(private http: HttpClient) {}

  getAll(): Observable<Room[]> {
    return this.http.get<Room[]>(this.baseUrl)
      .pipe(catchError(this.handleError));
  }
 
  getPhotoById(roomId: number): Observable<any> {
    return this.http.get<any>(`${this.baseUrl}/${roomId}/photos`);
  }
  getById(id: number): Observable<Room> {
    return this.http.get<Room>(`${this.baseUrl}/${id}`)
      .pipe(catchError(this.handleError));
  }

  create(formData: FormData): Observable<Room> {
    return this.http.post<Room>(this.baseUrl, formData, {
      headers: new HttpHeaders({
        Accept: 'application/json'
      })
    }).pipe(catchError(this.handleError));
  }

  // update(id: number, room: Room): Observable<Room> {
  //   return this.http.put<Room>(`${this.baseUrl}/${id}`, room)
  //     .pipe(catchError(this.handleError));
  // }
  update(id: number, formData: FormData): Observable<Room> {
    return this.http.put<Room>(`${this.baseUrl}/${id}`, formData);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`)
      .pipe(catchError(this.handleError));
  }

  private handleError(error: HttpErrorResponse) {
    let errorMessage = 'An error occurred';
    if (error.error instanceof ErrorEvent) {
      errorMessage = `Error: ${error.error.message}`;
    } else {
      errorMessage = `Error Code: ${error.status}\nMessage: ${error.message}`;
    }
    return throwError(() => new Error(errorMessage));
  }
}
