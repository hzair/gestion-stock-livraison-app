import dayjs from 'dayjs/esm';
import { ICatalogProduit } from 'app/entities/catalog-produit/catalog-produit.model';
import { IFactureAchat } from 'app/entities/facture-achat/facture-achat.model';
import { IFactureVente } from 'app/entities/facture-vente/facture-vente.model';

export interface ILotFacture {
  id?: number;
  quantite?: number | null;
  date?: dayjs.Dayjs | null;
  montantTotal?: number | null;
  produit?: ICatalogProduit;
  factureAchat?: IFactureAchat | null;
  factureVente?: IFactureVente | null;
}

export class LotFacture implements ILotFacture {
  constructor(
    public id?: number,
    public quantite?: number | null,
    public date?: dayjs.Dayjs | null,
    public montantTotal?: number | null,
    public produit?: ICatalogProduit,
    public factureAchat?: IFactureAchat | null,
    public factureVente?: IFactureVente | null
  ) {}
}

export function getLotFactureIdentifier(lotFacture: ILotFacture): number | undefined {
  return lotFacture.id;
}
