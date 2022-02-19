export interface IVendeur {
  id?: number;
  imageContentType?: string | null;
  image?: string | null;
  matricule?: string;
  designation?: string;
  nom?: string | null;
  prenom?: string | null;
  email?: string | null;
  adresse?: string | null;
  phoneNumber?: string | null;
  salaire?: number | null;
}

export class Vendeur implements IVendeur {
  constructor(
    public id?: number,
    public imageContentType?: string | null,
    public image?: string | null,
    public matricule?: string,
    public designation?: string,
    public nom?: string | null,
    public prenom?: string | null,
    public email?: string | null,
    public adresse?: string | null,
    public phoneNumber?: string | null,
    public salaire?: number | null
  ) {}
}

export function getVendeurIdentifier(vendeur: IVendeur): number | undefined {
  return vendeur.id;
}
