import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICatalogProduit, getCatalogProduitIdentifier } from '../catalog-produit.model';

export type EntityResponseType = HttpResponse<ICatalogProduit>;
export type EntityArrayResponseType = HttpResponse<ICatalogProduit[]>;

@Injectable({ providedIn: 'root' })
export class CatalogProduitService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/catalog-produits');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(catalogProduit: ICatalogProduit): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(catalogProduit);
    return this.http
      .post<ICatalogProduit>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(catalogProduit: ICatalogProduit): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(catalogProduit);
    return this.http
      .put<ICatalogProduit>(`${this.resourceUrl}/${getCatalogProduitIdentifier(catalogProduit) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(catalogProduit: ICatalogProduit): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(catalogProduit);
    return this.http
      .patch<ICatalogProduit>(`${this.resourceUrl}/${getCatalogProduitIdentifier(catalogProduit) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ICatalogProduit>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ICatalogProduit[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addCatalogProduitToCollectionIfMissing(
    catalogProduitCollection: ICatalogProduit[],
    ...catalogProduitsToCheck: (ICatalogProduit | null | undefined)[]
  ): ICatalogProduit[] {
    const catalogProduits: ICatalogProduit[] = catalogProduitsToCheck.filter(isPresent);
    if (catalogProduits.length > 0) {
      const catalogProduitCollectionIdentifiers = catalogProduitCollection.map(
        catalogProduitItem => getCatalogProduitIdentifier(catalogProduitItem)!
      );
      const catalogProduitsToAdd = catalogProduits.filter(catalogProduitItem => {
        const catalogProduitIdentifier = getCatalogProduitIdentifier(catalogProduitItem);
        if (catalogProduitIdentifier == null || catalogProduitCollectionIdentifiers.includes(catalogProduitIdentifier)) {
          return false;
        }
        catalogProduitCollectionIdentifiers.push(catalogProduitIdentifier);
        return true;
      });
      return [...catalogProduitsToAdd, ...catalogProduitCollection];
    }
    return catalogProduitCollection;
  }

  protected convertDateFromClient(catalogProduit: ICatalogProduit): ICatalogProduit {
    return Object.assign({}, catalogProduit, {
      date: catalogProduit.date?.isValid() ? catalogProduit.date.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.date = res.body.date ? dayjs(res.body.date) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((catalogProduit: ICatalogProduit) => {
        catalogProduit.date = catalogProduit.date ? dayjs(catalogProduit.date) : undefined;
      });
    }
    return res;
  }
}
