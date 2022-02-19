import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICatalogTypeProduit, getCatalogTypeProduitIdentifier } from '../catalog-type-produit.model';

export type EntityResponseType = HttpResponse<ICatalogTypeProduit>;
export type EntityArrayResponseType = HttpResponse<ICatalogTypeProduit[]>;

@Injectable({ providedIn: 'root' })
export class CatalogTypeProduitService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/catalog-type-produits');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(catalogTypeProduit: ICatalogTypeProduit): Observable<EntityResponseType> {
    return this.http.post<ICatalogTypeProduit>(this.resourceUrl, catalogTypeProduit, { observe: 'response' });
  }

  update(catalogTypeProduit: ICatalogTypeProduit): Observable<EntityResponseType> {
    return this.http.put<ICatalogTypeProduit>(
      `${this.resourceUrl}/${getCatalogTypeProduitIdentifier(catalogTypeProduit) as number}`,
      catalogTypeProduit,
      { observe: 'response' }
    );
  }

  partialUpdate(catalogTypeProduit: ICatalogTypeProduit): Observable<EntityResponseType> {
    return this.http.patch<ICatalogTypeProduit>(
      `${this.resourceUrl}/${getCatalogTypeProduitIdentifier(catalogTypeProduit) as number}`,
      catalogTypeProduit,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ICatalogTypeProduit>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICatalogTypeProduit[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addCatalogTypeProduitToCollectionIfMissing(
    catalogTypeProduitCollection: ICatalogTypeProduit[],
    ...catalogTypeProduitsToCheck: (ICatalogTypeProduit | null | undefined)[]
  ): ICatalogTypeProduit[] {
    const catalogTypeProduits: ICatalogTypeProduit[] = catalogTypeProduitsToCheck.filter(isPresent);
    if (catalogTypeProduits.length > 0) {
      const catalogTypeProduitCollectionIdentifiers = catalogTypeProduitCollection.map(
        catalogTypeProduitItem => getCatalogTypeProduitIdentifier(catalogTypeProduitItem)!
      );
      const catalogTypeProduitsToAdd = catalogTypeProduits.filter(catalogTypeProduitItem => {
        const catalogTypeProduitIdentifier = getCatalogTypeProduitIdentifier(catalogTypeProduitItem);
        if (catalogTypeProduitIdentifier == null || catalogTypeProduitCollectionIdentifiers.includes(catalogTypeProduitIdentifier)) {
          return false;
        }
        catalogTypeProduitCollectionIdentifiers.push(catalogTypeProduitIdentifier);
        return true;
      });
      return [...catalogTypeProduitsToAdd, ...catalogTypeProduitCollection];
    }
    return catalogTypeProduitCollection;
  }
}
