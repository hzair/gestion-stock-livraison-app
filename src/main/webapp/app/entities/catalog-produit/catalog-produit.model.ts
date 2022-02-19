import dayjs from 'dayjs/esm';
import { IFournisseur } from 'app/entities/fournisseur/fournisseur.model';
import { ICatalogTypeProduit } from 'app/entities/catalog-type-produit/catalog-type-produit.model';

export interface ICatalogProduit {
  id?: number;
  imageContentType?: string | null;
  image?: string | null;
  idFonc?: string | null;
  designation?: string;
  description?: string | null;
  quantite?: number;
  prixAchatUnit?: number;
  prixVenteUnit?: number;
  prixVenteGros?: number | null;
  date?: dayjs.Dayjs | null;
  fournisseur?: IFournisseur;
  type?: ICatalogTypeProduit;
}

export class CatalogProduit implements ICatalogProduit {
  constructor(
    public id?: number,
    public imageContentType?: string | null,
    public image?: string | null,
    public idFonc?: string | null,
    public designation?: string,
    public description?: string | null,
    public quantite?: number,
    public prixAchatUnit?: number,
    public prixVenteUnit?: number,
    public prixVenteGros?: number | null,
    public date?: dayjs.Dayjs | null,
    public fournisseur?: IFournisseur,
    public type?: ICatalogTypeProduit
  ) {}
}

export function getCatalogProduitIdentifier(catalogProduit: ICatalogProduit): number | undefined {
  return catalogProduit.id;
}
