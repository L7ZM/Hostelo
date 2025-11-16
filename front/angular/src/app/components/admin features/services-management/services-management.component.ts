import { Component, OnInit } from '@angular/core';
import { MessageService } from 'primeng/api';
import { ConfirmationService } from 'primeng/api'; // Import the confirmation service
import { ServicesManageService } from 'src/app/services/servicesManagement/services-manage.service'; // Import the service

@Component({
  selector: 'app-services-management',
  templateUrl: './services-management.component.html',
  styleUrls: ['./services-management.component.scss'],
  providers: [MessageService, ConfirmationService], // Add ConfirmationService to providers
})
export class ServicesManagementComponent implements OnInit {
  services: any[] = [];
  serviceDialog: boolean = false;
  service: any = {};
  submitted: boolean = false;

  constructor(
    private messageService: MessageService,
    private confirmationService: ConfirmationService, // Inject the ConfirmationService
    private serviceService: ServicesManageService // Inject the service
  ) {}

  ngOnInit(): void {
    this.loadServices(); // Load services from the API when the component initializes
  }

  // Load all services from the API
  loadServices() {
    this.serviceService.getAllServices().subscribe(
      (data) => {
        this.services = data;
      },
      (error) => {
        console.error('Error loading services', error);
        this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Could not load services' });
      }
    );
  }

  // Open dialog for new service
  openNew() {
    this.service = {};
    this.submitted = false;
    this.serviceDialog = true;
  }

  // Hide the service dialog
  hideDialog() {
    this.serviceDialog = false;
  }

  // Save the service (add or update)
  saveService() {
    this.submitted = true;

    if (this.service.nomService && this.service.description && this.service.prix) {
      if (this.service.id) {
        // Update existing service
        this.confirmationService.confirm({
          message: 'Are you sure you want to update this service?',
          header: 'Confirmation',
          icon: 'pi pi-exclamation-triangle',
          accept: () => {
            this.serviceService.updateService(this.service).subscribe(
              () => {
                this.loadServices();
                this.serviceDialog = false;
                this.messageService.add({ severity: 'success', summary: 'Updated', detail: 'Service updated successfully' });
              },
              () => this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Error updating service' })
            );
          }
        });
      } else {
        // Add new service
        this.serviceService.addService(this.service).subscribe(
          () => {
            this.loadServices();
            this.serviceDialog = false;
            this.messageService.add({ severity: 'success', summary: 'Added', detail: 'Service added successfully' });
          },
          () => this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Error adding service' })
        );
      }
    } else {
      this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Please fill all fields' });
    }
  }


  // Edit an existing service
  editService(service: any) {
    console.log('Editing service:', service); // Ensure the service data is correct
    this.service = { ...service }; // Make sure the selected service is correctly passed to the form
    this.serviceDialog = true;
  }


// Delete a service with confirmation
deleteService(service: any) {
  this.confirmationService.confirm({
    message: 'Are you sure you want to delete this service?',
    header: 'Confirmation',
    icon: 'pi pi-exclamation-triangle',
    accept: () => {
      // This block is executed when the user clicks "Yes"
      this.serviceService.deleteService(service.id).subscribe(
        () => {
          this.loadServices();
          this.messageService.add({ severity: 'success', summary: 'Deleted', detail: 'Service deleted successfully' });
        },
        (error) => {
          this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Error deleting service' });
        }
      );
    },
    reject: () => {
      // This block is executed when the user clicks "No"
      this.messageService.add({ severity: 'info', summary: 'Cancelled', detail: 'Service deletion was cancelled' });
    }
  });
}
}
