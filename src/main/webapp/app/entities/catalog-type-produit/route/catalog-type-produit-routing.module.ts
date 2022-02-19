import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CatalogTypeProduitComponent } from '../list/catalog-type-produit.component';
import { CatalogTypeProduitDetailComponent } from '../detail/catalog-type-produit-detail.component';
import { CatalogTypeProduitUpdateComponent } from '../update/catalog-type-produit-update.component';
import { CatalogTypeProduitRoutingResolveService } from './catalog-type-produit-routing-resolve.service';

const catalogTypeProduitRoute: Routes = [
  {
    path: '',
    component: CatalogTypeProduitComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CatalogTypeProduitDetailComponent,
    resolve: {
      catalogTypeProduit: CatalogTypeProduitRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CatalogTypeProduitUpdateComponent,
    resolve: {
      catalogTypeProduit: CatalogTypeProduitRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CatalogTypeProduitUpdateComponent,
    resolve: {
      catalogTypeProduit: CatalogTypeProduitRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(catalogTypeProduitRoute)],
  exports: [RouterModule],
})
export class CatalogTypeProduitRoutingModule {}
