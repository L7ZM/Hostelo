import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { CustomerDTO } from '../../models/customer/customer-dto';
import { environment } from 'src/environments/environment';
import { CustomerRegistrationRequest } from '../../models/customer/customer-registration-request';
import { CustomerUpdateRequest } from '../../models/customer/customer-update-request';

@Injectable({
  providedIn: 'root'
})
export class CustomerService {

  private readonly customerUrl = `${environment.api.baseUrl}/${environment.api.adminUrl}`;

  constructor(
    private http: HttpClient
  ) { }

  findAll(): Observable<CustomerDTO[]> {
    return this.http.get<CustomerDTO[]>(this.customerUrl);
  }
  // Method to create a customer
  createCustomer(
    customer: CustomerRegistrationRequest,
  ): Observable<any> {
    return this.http.post(`http://localhost:8080/api/admin/createUser`, customer);
  }

  deleteCustomer(email: string | undefined): Observable<void> {
    return this.http.delete<void>(`${this.customerUrl}/${email}`);
  }
  updateCustomer(
    id: number,
    customer: CustomerDTO,
    token: string
  ): Observable<CustomerDTO> {
    return this.http.put<CustomerDTO>(`http://localhost:8080/api/admin/updateUser`, customer, {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    });
  }

  updateCurrentCustomer(
    customer: CustomerDTO,
    token: string
  ): Observable<CustomerDTO> {
    return this.http.put<CustomerDTO>(`http://localhost:8080/api/user/update`, customer, {
      headers: {
        'Authorization': `Bearer ${token}`,
        'Content-Type': 'application/json'  // Ensure Content-Type is set to application/json
      },
    });
  }

}
