import { IFactureAchat } from 'app/entities/facture-achat/facture-achat.model';

export interface IFournisseur {
  id?: number;
  imageContentType?: string | null;
  image?: string | null;
  matricule?: string;
  designation?: string;
  nom?: string | null;
  prenom?: string | null;
  email?: string | null;
  phoneNumber?: string | null;
  adresse?: string | null;
  factures?: IFactureAchat[] | null;
}

export class Fournisseur implements IFournisseur {
  constructor(
    public id?: number,
    public imageContentType?: string | null,
    public image?: string | null,
    public matricule?: string,
    public designation?: string,
    public nom?: string | null,
    public prenom?: string | null,
    public email?: string | null,
    public phoneNumber?: string | null,
    public adresse?: string | null,
    public factures?: IFactureAchat[] | null
  ) {}
}

export function getFournisseurIdentifier(fournisseur: IFournisseur): number | undefined {
  return fournisseur.id;
}
