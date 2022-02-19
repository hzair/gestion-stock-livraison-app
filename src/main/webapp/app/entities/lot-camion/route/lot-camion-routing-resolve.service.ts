import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ILotCamion, LotCamion } from '../lot-camion.model';
import { LotCamionService } from '../service/lot-camion.service';

@Injectable({ providedIn: 'root' })
export class LotCamionRoutingResolveService implements Resolve<ILotCamion> {
  constructor(protected service: LotCamionService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ILotCamion> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((lotCamion: HttpResponse<LotCamion>) => {
          if (lotCamion.body) {
            return of(lotCamion.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new LotCamion());
  }
}
