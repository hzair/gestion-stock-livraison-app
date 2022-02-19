import { ICatalogProduit } from 'app/entities/catalog-produit/catalog-produit.model';

export interface ICatalogTypeProduit {
  id?: number;
  imageContentType?: string | null;
  image?: string | null;
  idFonc?: string | null;
  designation?: string;
  description?: string | null;
  catalogProduits?: ICatalogProduit[] | null;
}

export class CatalogTypeProduit implements ICatalogTypeProduit {
  constructor(
    public id?: number,
    public imageContentType?: string | null,
    public image?: string | null,
    public idFonc?: string | null,
    public designation?: string,
    public description?: string | null,
    public catalogProduits?: ICatalogProduit[] | null
  ) {}
}

export function getCatalogTypeProduitIdentifier(catalogTypeProduit: ICatalogTypeProduit): number | undefined {
  return catalogTypeProduit.id;
}
