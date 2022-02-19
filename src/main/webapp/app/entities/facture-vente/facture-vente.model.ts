import dayjs from 'dayjs/esm';
import { ILotFacture } from 'app/entities/lot-facture/lot-facture.model';
import { IClient } from 'app/entities/client/client.model';

export interface IFactureVente {
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
  client?: IClient;
}

export class FactureVente implements IFactureVente {
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
    public client?: IClient
  ) {
    this.payee = this.payee ?? false;
  }
}

export function getFactureVenteIdentifier(factureVente: IFactureVente): number | undefined {
  return factureVente.id;
}
