

export interface CustomerDTO {

  id?: number;
  nom?: string;
  prenom?: string;
  email?: string;
  adresse?: string ;
  telephone?: string ;
  pointsFidelite?: number;
  dateNaissance? : Date;
  roles?: string[],
  username?: string;
  password?:string
  authorities?: { authority: string }[];
}
