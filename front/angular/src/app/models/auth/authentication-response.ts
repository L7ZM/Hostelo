import { CustomerDTO } from './../customer/customer-dto';


export interface AuthenticationResponse {
  token: string;
  customerDTO: CustomerDTO;
  roles:string[];
  nom:string ;
  prenom:string;
  email:string;
  dateNaissance: Date;
  adresse: string;
  telephone: number;



}
