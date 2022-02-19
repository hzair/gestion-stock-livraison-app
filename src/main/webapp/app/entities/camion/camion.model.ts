import dayjs from 'dayjs/esm';
import { IVendeur } from 'app/entities/vendeur/vendeur.model';
import { ILotCamion } from 'app/entities/lot-camion/lot-camion.model';

export interface ICamion {
  id?: number;
  designation?: string | null;
  date?: dayjs.Dayjs | null;
  montantTotal?: number | null;
  vendeur?: IVendeur;
  produits?: ILotCamion[] | null;
}

export class Camion implements ICamion {
  constructor(
    public id?: number,
    public designation?: string | null,
    public date?: dayjs.Dayjs | null,
    public montantTotal?: number | null,
    public vendeur?: IVendeur,
    public produits?: ILotCamion[] | null
  ) {}
}

export function getCamionIdentifier(camion: ICamion): number | undefined {
  return camion.id;
}
