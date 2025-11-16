export interface Room {

    id?: number;
    numeroChambre: number;
    etat: string;
    type: string;
    prix: number;
    description: string;

    photos: File[]; // Array of base64 strings
}
