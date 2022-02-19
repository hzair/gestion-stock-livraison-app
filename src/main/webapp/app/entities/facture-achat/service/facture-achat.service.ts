import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IFactureAchat, getFactureAchatIdentifier } from '../facture-achat.model';

export type EntityResponseType = HttpResponse<IFactureAchat>;
export type EntityArrayResponseType = HttpResponse<IFactureAchat[]>;

@Injectable({ providedIn: 'root' })
export class FactureAchatService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/facture-achats');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(factureAchat: IFactureAchat): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(factureAchat);
    return this.http
      .post<IFactureAchat>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(factureAchat: IFactureAchat): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(factureAchat);
    return this.http
      .put<IFactureAchat>(`${this.resourceUrl}/${getFactureAchatIdentifier(factureAchat) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(factureAchat: IFactureAchat): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(factureAchat);
    return this.http
      .patch<IFactureAchat>(`${this.resourceUrl}/${getFactureAchatIdentifier(factureAchat) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IFactureAchat>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IFactureAchat[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addFactureAchatToCollectionIfMissing(
    factureAchatCollection: IFactureAchat[],
    ...factureAchatsToCheck: (IFactureAchat | null | undefined)[]
  ): IFactureAchat[] {
    const factureAchats: IFactureAchat[] = factureAchatsToCheck.filter(isPresent);
    if (factureAchats.length > 0) {
      const factureAchatCollectionIdentifiers = factureAchatCollection.map(
        factureAchatItem => getFactureAchatIdentifier(factureAchatItem)!
      );
      const factureAchatsToAdd = factureAchats.filter(factureAchatItem => {
        const factureAchatIdentifier = getFactureAchatIdentifier(factureAchatItem);
        if (factureAchatIdentifier == null || factureAchatCollectionIdentifiers.includes(factureAchatIdentifier)) {
          return false;
        }
        factureAchatCollectionIdentifiers.push(factureAchatIdentifier);
        return true;
      });
      return [...factureAchatsToAdd, ...factureAchatCollection];
    }
    return factureAchatCollection;
  }

  protected convertDateFromClient(factureAchat: IFactureAchat): IFactureAchat {
    return Object.assign({}, factureAchat, {
      date: factureAchat.date?.isValid() ? factureAchat.date.toJSON() : undefined,
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
      res.body.forEach((factureAchat: IFactureAchat) => {
        factureAchat.date = factureAchat.date ? dayjs(factureAchat.date) : undefined;
      });
    }
    return res;
  }
}
