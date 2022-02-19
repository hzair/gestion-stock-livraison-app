import dayjs from 'dayjs/esm';
import { ICatalogProduit } from 'app/entities/catalog-produit/catalog-produit.model';
import { ICamion } from 'app/entities/camion/camion.model';

export interface ILotCamion {
  id?: number;
  quantite?: number | null;
  date?: dayjs.Dayjs | null;
  montantTotal?: number | null;
  produit?: ICatalogProduit;
  camion?: ICamion;
}

export class LotCamion implements ILotCamion {
  constructor(
    public id?: number,
    public quantite?: number | null,
    public date?: dayjs.Dayjs | null,
    public montantTotal?: number | null,
    public produit?: ICatalogProduit,
    public camion?: ICamion
  ) {}
}

export function getLotCamionIdentifier(lotCamion: ILotCamion): number | undefined {
  return lotCamion.id;
}
