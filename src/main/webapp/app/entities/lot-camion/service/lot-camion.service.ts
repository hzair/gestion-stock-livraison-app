import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ILotCamion, getLotCamionIdentifier } from '../lot-camion.model';

export type EntityResponseType = HttpResponse<ILotCamion>;
export type EntityArrayResponseType = HttpResponse<ILotCamion[]>;

@Injectable({ providedIn: 'root' })
export class LotCamionService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/lot-camions');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(lotCamion: ILotCamion): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(lotCamion);
    return this.http
      .post<ILotCamion>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(lotCamion: ILotCamion): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(lotCamion);
    return this.http
      .put<ILotCamion>(`${this.resourceUrl}/${getLotCamionIdentifier(lotCamion) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(lotCamion: ILotCamion): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(lotCamion);
    return this.http
      .patch<ILotCamion>(`${this.resourceUrl}/${getLotCamionIdentifier(lotCamion) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ILotCamion>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ILotCamion[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addLotCamionToCollectionIfMissing(
    lotCamionCollection: ILotCamion[],
    ...lotCamionsToCheck: (ILotCamion | null | undefined)[]
  ): ILotCamion[] {
    const lotCamions: ILotCamion[] = lotCamionsToCheck.filter(isPresent);
    if (lotCamions.length > 0) {
      const lotCamionCollectionIdentifiers = lotCamionCollection.map(lotCamionItem => getLotCamionIdentifier(lotCamionItem)!);
      const lotCamionsToAdd = lotCamions.filter(lotCamionItem => {
        const lotCamionIdentifier = getLotCamionIdentifier(lotCamionItem);
        if (lotCamionIdentifier == null || lotCamionCollectionIdentifiers.includes(lotCamionIdentifier)) {
          return false;
        }
        lotCamionCollectionIdentifiers.push(lotCamionIdentifier);
        return true;
      });
      return [...lotCamionsToAdd, ...lotCamionCollection];
    }
    return lotCamionCollection;
  }

  protected convertDateFromClient(lotCamion: ILotCamion): ILotCamion {
    return Object.assign({}, lotCamion, {
      date: lotCamion.date?.isValid() ? lotCamion.date.toJSON() : undefined,
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
      res.body.forEach((lotCamion: ILotCamion) => {
        lotCamion.date = lotCamion.date ? dayjs(lotCamion.date) : undefined;
      });
    }
    return res;
  }
}
