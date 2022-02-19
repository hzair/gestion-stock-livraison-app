import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IFactureAchat, FactureAchat } from '../facture-achat.model';
import { FactureAchatService } from '../service/facture-achat.service';

@Injectable({ providedIn: 'root' })
export class FactureAchatRoutingResolveService implements Resolve<IFactureAchat> {
  constructor(protected service: FactureAchatService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IFactureAchat> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((factureAchat: HttpResponse<FactureAchat>) => {
          if (factureAchat.body) {
            return of(factureAchat.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new FactureAchat());
  }
}
