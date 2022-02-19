import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICatalogTypeProduit, CatalogTypeProduit } from '../catalog-type-produit.model';
import { CatalogTypeProduitService } from '../service/catalog-type-produit.service';

@Injectable({ providedIn: 'root' })
export class CatalogTypeProduitRoutingResolveService implements Resolve<ICatalogTypeProduit> {
  constructor(protected service: CatalogTypeProduitService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICatalogTypeProduit> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((catalogTypeProduit: HttpResponse<CatalogTypeProduit>) => {
          if (catalogTypeProduit.body) {
            return of(catalogTypeProduit.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new CatalogTypeProduit());
  }
}
