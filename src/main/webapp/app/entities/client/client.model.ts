import { IFactureVente } from 'app/entities/facture-vente/facture-vente.model';

export interface IClient {
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
  factures?: IFactureVente[] | null;
}

export class Client implements IClient {
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
    public factures?: IFactureVente[] | null
  ) {}
}

export function getClientIdentifier(client: IClient): number | undefined {
  return client.id;
}
