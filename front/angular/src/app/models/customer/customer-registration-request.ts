

export interface CustomerRegistrationRequest {
  id?: number;
  nom?: string;
  prenom?: string;
  email?: string;
  password?: string;
  telephone?: string;
  adresse?: string;
  pointsFidelite?: number;
  dateNaissance?: Date;
  authorities?: { authority: string }[];
}
