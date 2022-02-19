import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ILotFacture, LotFacture } from '../lot-facture.model';
import { LotFactureService } from '../service/lot-facture.service';

@Injectable({ providedIn: 'root' })
export class LotFactureRoutingResolveService implements Resolve<ILotFacture> {
  constructor(protected service: LotFactureService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ILotFacture> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((lotFacture: HttpResponse<LotFacture>) => {
          if (lotFacture.body) {
            return of(lotFacture.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new LotFacture());
  }
}
