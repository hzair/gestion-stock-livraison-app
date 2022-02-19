import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CatalogProduitComponent } from '../list/catalog-produit.component';
import { CatalogProduitDetailComponent } from '../detail/catalog-produit-detail.component';
import { CatalogProduitUpdateComponent } from '../update/catalog-produit-update.component';
import { CatalogProduitRoutingResolveService } from './catalog-produit-routing-resolve.service';

const catalogProduitRoute: Routes = [
  {
    path: '',
    component: CatalogProduitComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CatalogProduitDetailComponent,
    resolve: {
      catalogProduit: CatalogProduitRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CatalogProduitUpdateComponent,
    resolve: {
      catalogProduit: CatalogProduitRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CatalogProduitUpdateComponent,
    resolve: {
      catalogProduit: CatalogProduitRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(catalogProduitRoute)],
  exports: [RouterModule],
})
export class CatalogProduitRoutingModule {}
