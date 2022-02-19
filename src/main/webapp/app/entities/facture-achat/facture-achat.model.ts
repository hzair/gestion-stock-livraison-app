import dayjs from 'dayjs/esm';
import { ILotFacture } from 'app/entities/lot-facture/lot-facture.model';
import { IFournisseur } from 'app/entities/fournisseur/fournisseur.model';

export interface IFactureAchat {
  id?: number;
  photoContentType?: string | null;
  photo?: string | null;
  idFonc?: string | null;
  designation?: string;
  description?: string | null;
  montant?: number;
  date?: dayjs.Dayjs | null;
  payee?: boolean | null;
  produits?: ILotFacture[];
  fournisseur?: IFournisseur;
}

export class FactureAchat implements IFactureAchat {
  constructor(
    public id?: number,
    public photoContentType?: string | null,
    public photo?: string | null,
    public idFonc?: string | null,
    public designation?: string,
    public description?: string | null,
    public montant?: number,
    public date?: dayjs.Dayjs | null,
    public payee?: boolean | null,
    public produits?: ILotFacture[],
    public fournisseur?: IFournisseur
  ) {
    this.payee = this.payee ?? false;
  }
}

export function getFactureAchatIdentifier(factureAchat: IFactureAchat): number | undefined {
  return factureAchat.id;
}
