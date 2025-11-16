export interface Reservation {
    id: number;  
    numeroChambre: number; 
    dateDebut: string;      
    dateFin: string;       
    idUser: number;      
    status: string;        
    totalPrice?: number;    
    services?: string[];   
    nom: string;
    prenom: string; 
  }