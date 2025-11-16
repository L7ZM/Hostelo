import { Component, EventEmitter, Input, Output } from '@angular/core';
import { CustomerDTO } from '../../../../models/customer/customer-dto';

@Component({
  selector: 'app-customer-card',
  templateUrl: './customer-card.component.html',
  styleUrls: ['./customer-card.component.scss']
})
export class CustomerCardComponent {

  @Input()
  customer: CustomerDTO = {
    pointsFidelite: 0
  };
  @Input()
  customerIndex = 0;

  @Output()
  delete: EventEmitter<CustomerDTO> = new EventEmitter<CustomerDTO>();
  @Output()
  update: EventEmitter<CustomerDTO> = new EventEmitter<CustomerDTO>();
searchQuery: any;


  onDelete() {
    this.delete.emit(this.customer);
  }
  onUpdate() {
    this.update.emit(this.customer);
  }
}
