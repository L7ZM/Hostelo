import { Component } from '@angular/core';
import { ConfirmationService, MessageService } from 'primeng/api';
import { Room } from 'src/app/models/room/room.model';
import { RoomService } from 'src/app/services/rooms/room-service.service';

@Component({
  selector: 'app-room',
  templateUrl: './room-management.component.html',
  styleUrls: ['./room-management.component.scss'],
  providers: [MessageService,ConfirmationService]
})
export class RoomManagementComponent {

  rooms: Room[] = [];
  room: Room = {
    numeroChambre: 0,
    etat: '',
    type: '',
    prix: 0,
    description: '',
    photos: []
  };

  photo: File[] = [];
  selectedRoom: Room | null = null;
  displayDialog = false;
  isNew = false;

  typeOptions = [
    { label: 'SINGLE', value: 'SINGLE' },
    { label: 'DOUBLE', value: 'DOUBLE' },
    { label: 'DELUXE', value: 'DELUXE' }
  ];

  etatOptions = [
    { label: 'DISPONIBLE', value: 'DISPONIBLE' },
    { label: 'OCCUPE', value: 'OCCUPEE' },
    { label: 'MAINTENANCE', value: 'EN_ENTRETIEN' }
  ];

  responsiveOptions: any[] = [
    {
        breakpoint: '1024px',
        numVisible: 5
    },
    {
      breakpoint: '768px',
        numVisible: 3

    },
    {
        breakpoint: '560px',

        numVisible: 1
    }
];

  constructor(
    private roomService: RoomService,
    private messageService: MessageService,
    private confirmationService: ConfirmationService,
  ) {
    // this.loadRooms();
  }

  ngOnInit(): void {
    this.loadRooms();
  }

  loadRooms() {
    this.roomService.getAll().subscribe({
      next: (data) => this.rooms = data,
      error: () => this.messageService.add({
        severity: 'error',
        summary: 'Error',
        detail: 'Failed to load rooms'
      })
    });
  }

  openNew() {
    this.room = {
      numeroChambre: 0,
      etat: '',
      type: '',
      prix: 0,
      description: '',
      photos: []
    };
    this.photo = [];
    this.isNew = true;
    this.displayDialog = true;
  }

  editRoom(room: Room) {
    this.room = { ...room };
    this.photo = [];
    this.isNew = false;
    this.displayDialog = true;
  }

  deleteRoom(room: Room) {
    this.confirmationService.confirm({
      message: 'Are you sure you want to delete this room?',
      header: 'Confirm Deletion',
      icon: 'pi pi-exclamation-triangle',
      accept: () => {
        this.roomService.delete(room.id!).subscribe({
          next: () => {
            this.messageService.add({
              severity: 'success',
              summary: 'Success',
              detail: 'Room deleted successfully'
            });
            this.loadRooms();
          },
          error: () => this.messageService.add({
            severity: 'error',
            summary: 'Error',
            detail: 'Failed to delete room'
          })
        });
      },
      reject: () => {
        this.messageService.add({
          severity: 'info',
          summary: 'Cancelled',
          detail: 'Room deletion cancelled'
        });
      }
    });
  }

  onPhotoSelect(event: any) {
    const files: FileList = event.files;
    if (files && files.length > 0) {
      Array.from(files).forEach((file: File) => {
        this.photo.push(file); 
      });
    }
  }


  removePhoto(index: number) {
    this.room.photos.splice(index, 1);
  }

  saveRoom() {
    console.log("your in save method")
      const formData = new FormData();
      // formData.append('numeroChambre', this.room.numeroChambre.toString());
      // formData.append('type', this.room.type);
      // formData.append('etat', this.room.etat);
      // formData.append('prix', this.room.prix.toString());
      // formData.append('description', this.room.description);

      // ----------------
      const chambreJson = JSON.stringify({
        numeroChambre: this.room.numeroChambre,
        type: this.room.type,
        etat: this.room.etat,
        prix: this.room.prix,
        description: this.room.description
      });
    
      formData.append('chambre', chambreJson);
    
      this.photo.forEach((photo) => {
        formData.append('photo', photo); 
      });

      if (this.isNew) {
      this.roomService.create(formData).subscribe({
        next: (room) => {
          this.messageService.add({
            severity: 'success',
            summary: 'Success',
            detail: 'Room created successfully'
          });
          this.loadRooms(); 
          this.displayDialog = false; 
        },
        error: (error) => {
          console.error("Error creating room:", error);
          this.messageService.add({
            severity: 'error',
            summary: 'Error',
            detail: 'Failed to create room'
          });
        }
      });
    }else{
        this.roomService.update(this.room.id!, formData).subscribe({
          next: () => {
            this.messageService.add({
              severity: 'success',
              summary: 'Success',
              detail: 'Room updated successfully'
            });
            this.loadRooms();
            this.displayDialog = false;
          },
          error: (error) => {
            console.error('Error updating room:', error);
            this.messageService.add({
              severity: 'error',
              summary: 'Error',
              detail: 'Failed to update room'
            });
      }
    });
  }
    

}}
