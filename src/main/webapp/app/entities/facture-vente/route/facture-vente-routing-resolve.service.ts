import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IFactureVente, FactureVente } from '../facture-vente.model';
import { FactureVenteService } from '../service/facture-vente.service';

@Injectable({ providedIn: 'root' })
export class FactureVenteRoutingResolveService implements Resolve<IFactureVente> {
  constructor(protected service: FactureVenteService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IFactureVente> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((factureVente: HttpResponse<FactureVente>) => {
          if (factureVente.body) {
            return of(factureVente.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new FactureVente());
  }
}
