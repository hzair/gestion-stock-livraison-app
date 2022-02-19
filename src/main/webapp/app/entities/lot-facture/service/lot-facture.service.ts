import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ILotFacture, getLotFactureIdentifier } from '../lot-facture.model';

export type EntityResponseType = HttpResponse<ILotFacture>;
export type EntityArrayResponseType = HttpResponse<ILotFacture[]>;

@Injectable({ providedIn: 'root' })
export class LotFactureService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/lot-factures');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(lotFacture: ILotFacture): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(lotFacture);
    return this.http
      .post<ILotFacture>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(lotFacture: ILotFacture): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(lotFacture);
    return this.http
      .put<ILotFacture>(`${this.resourceUrl}/${getLotFactureIdentifier(lotFacture) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(lotFacture: ILotFacture): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(lotFacture);
    return this.http
      .patch<ILotFacture>(`${this.resourceUrl}/${getLotFactureIdentifier(lotFacture) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ILotFacture>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ILotFacture[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addLotFactureToCollectionIfMissing(
    lotFactureCollection: ILotFacture[],
    ...lotFacturesToCheck: (ILotFacture | null | undefined)[]
  ): ILotFacture[] {
    const lotFactures: ILotFacture[] = lotFacturesToCheck.filter(isPresent);
    if (lotFactures.length > 0) {
      const lotFactureCollectionIdentifiers = lotFactureCollection.map(lotFactureItem => getLotFactureIdentifier(lotFactureItem)!);
      const lotFacturesToAdd = lotFactures.filter(lotFactureItem => {
        const lotFactureIdentifier = getLotFactureIdentifier(lotFactureItem);
        if (lotFactureIdentifier == null || lotFactureCollectionIdentifiers.includes(lotFactureIdentifier)) {
          return false;
        }
        lotFactureCollectionIdentifiers.push(lotFactureIdentifier);
        return true;
      });
      return [...lotFacturesToAdd, ...lotFactureCollection];
    }
    return lotFactureCollection;
  }

  protected convertDateFromClient(lotFacture: ILotFacture): ILotFacture {
    return Object.assign({}, lotFacture, {
      date: lotFacture.date?.isValid() ? lotFacture.date.toJSON() : undefined,
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
      res.body.forEach((lotFacture: ILotFacture) => {
        lotFacture.date = lotFacture.date ? dayjs(lotFacture.date) : undefined;
      });
    }
    return res;
  }
}
