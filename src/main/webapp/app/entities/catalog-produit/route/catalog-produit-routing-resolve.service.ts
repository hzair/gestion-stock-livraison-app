import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICatalogProduit, CatalogProduit } from '../catalog-produit.model';
import { CatalogProduitService } from '../service/catalog-produit.service';

@Injectable({ providedIn: 'root' })
export class CatalogProduitRoutingResolveService implements Resolve<ICatalogProduit> {
  constructor(protected service: CatalogProduitService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICatalogProduit> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((catalogProduit: HttpResponse<CatalogProduit>) => {
          if (catalogProduit.body) {
            return of(catalogProduit.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new CatalogProduit());
  }
}
