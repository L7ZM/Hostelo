import { Component, EventEmitter, Input, Output, OnInit } from '@angular/core';
import { CustomerRegistrationRequest } from '../../../../models/customer/customer-registration-request';

@Component({
  selector: 'app-manage-customer',
  templateUrl: './manage-customer.component.html',
  styleUrls: ['./manage-customer.component.scss']
})
export class ManageCustomerComponent implements OnInit {
  @Input()
  customer: CustomerRegistrationRequest = { authorities: [] }; // Initialize with empty authorities
  @Input()
  operation: 'create' | 'update' = 'create';
  @Output()
  submit: EventEmitter<CustomerRegistrationRequest> = new EventEmitter<CustomerRegistrationRequest>();
  @Output()
  cancel: EventEmitter<void> = new EventEmitter<void>();

  authoritiesOptions = [
    { label: 'USER', value: 'ROLE_USER' },
    { label: 'ADMIN', value: 'ROLE_ADMIN' }
  ];

  ngOnInit() {
    if (!this.customer) {
      this.customer = { authorities: [] };  // Ensure authorities is initialized
    }
    console.log('Customer on init:', this.customer);
    console.log('Authorities:', this.customer.authorities);
  }


  get isCustomerValid(): boolean {
    // Initialize with false
    let valid = false;

    // Check if all required fields are filled and authorities are selected
    if (
      this.hasLength(this.customer.nom) &&
      this.hasLength(this.customer.prenom) &&
      this.hasLength(this.customer.email) &&
      (this.operation === 'update' || this.hasLength(this.customer.password)) &&
      this.customer.authorities && this.customer.authorities.length > 0
    ) {
      valid = true; // Set valid to true if all conditions are met
    }

    return valid; // Always returns a boolean (true or false)
  }



  private hasLength(input: string | undefined): boolean {
    return input !== null && input !== undefined && input.length > 0;
  }

  onSubmit() {
    this.submit.emit(this.customer); // Emit customer data on submit
  }

  onCancel() {
    this.cancel.emit(); // Emit cancel event
  }
}
