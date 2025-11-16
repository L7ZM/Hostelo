import { Component, OnInit } from '@angular/core';
import { CustomerDTO } from '../../../../models/customer/customer-dto';
import { CustomerService } from '../../../../services/customer/customer.service';
import { CustomerRegistrationRequest } from '../../../../models/customer/customer-registration-request';
import { ConfirmationService, MessageService } from 'primeng/api';
import { AuthenticationService } from 'src/app/services/authentication/authentication.service';

@Component({
  selector: 'app-customer',
  templateUrl: './customer.component.html',
  styleUrls: ['./customer.component.scss']
})
export class CustomerComponent implements OnInit {

  display = false;
  operation: 'create' | 'update' = 'create';
  customers: Array<CustomerDTO> = [];
  customer: CustomerRegistrationRequest = {};

  constructor(
    private customerService: CustomerService,
    private messageService: MessageService,
    private confirmationService: ConfirmationService,
    private authService: AuthenticationService
  ) {}

  ngOnInit(): void {
    this.findAllCustomers();
  }

  private findAllCustomers() {
    this.customerService.findAll()
    .subscribe({
      next: (data) => {
        this.customers = data;
        console.log(data);
      }
    })
  }

  save(customer: CustomerRegistrationRequest) {
    const token = this.authService.getToken(); // Get the token
    if (!token) {
      this.messageService.add({
        severity: 'error',
        summary: 'Unauthorized',
        detail: 'No authentication token found!',
      });
      return;
    }

    if (this.operation === 'create') {
      // Handle creating the customer
      customer.pointsFidelite = 0;
      this.customerService.createCustomer(customer as CustomerDTO).subscribe({
        next: () => {
          this.findAllCustomers();
          this.display = false;
          this.customer = {};
          this.messageService.add({
            severity: 'success',
            summary: 'Customer created',
            detail: `Customer ${customer.nom} was successfully created`,
          });
        },
        error: (err) => {
          this.messageService.add({
            severity: 'error',
            summary: 'Error',
            detail: `Failed to create customer: ${err.message}`,
          });
        }
      });
    } else if (this.operation === 'update' && customer.id !== undefined) {
      // Handle updating the customer
      customer.pointsFidelite = 0;
      this.customerService.updateCustomer(customer.id!, customer as CustomerDTO, token).subscribe({
        next: () => {
          this.findAllCustomers();
          this.display = false;
          this.customer = {};
          this.messageService.add({
            severity: 'success',
            summary: 'Customer updated',
            detail: `Customer ${customer.nom} was successfully updated`,
          });
        },
        error: (err) => {
          this.messageService.add({
            severity: 'error',
            summary: 'Error',
            detail: `Failed to update customer: ${err.message}`,
          });
        }
      });
    } else {
      this.messageService.add({
        severity: 'error',
        summary: 'Error',
        detail: 'Customer ID is missing!',
      });
    }
  }

  deleteCustomer(customer: CustomerDTO) {
    this.confirmationService.confirm({
      header: 'Delete customer',
      message: `Are you sure you want to delete ${customer.nom}? You can\'t undo this action afterward`,
      accept: () => {
        this.customerService.deleteCustomer(customer.email)
        .subscribe({
          next: () => {
            this.findAllCustomers();
            this.messageService.add({
              severity: 'success',
              summary: 'Customer deleted',
              detail: `Customer ${customer.nom} was successfully deleted`,
            });
          },
          error: (err) => {
            this.messageService.add({
              severity: 'error',
              summary: 'Error',
              detail: `Failed to delete customer: ${err.message}`,
            });
          }
        });
      }
    });
  }

  updateCustomer(customerDTO: CustomerDTO) {
    this.display = true;
    this.customer = customerDTO; // Populate the form with customer data
    this.operation = 'update'; // Set operation to update for form handling
  }

  createCustomer() {
    this.display = true;
    this.customer = {}; // Reset the customer object
    this.operation = 'create'; // Set operation to create for form handling
  }

  cancel() {
    this.display = false;
    this.customer = {};
    this.operation = 'create';
  }
}
