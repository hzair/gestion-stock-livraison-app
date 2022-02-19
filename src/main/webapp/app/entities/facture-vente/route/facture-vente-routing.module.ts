import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { FactureVenteComponent } from '../list/facture-vente.component';
import { FactureVenteDetailComponent } from '../detail/facture-vente-detail.component';
import { FactureVenteUpdateComponent } from '../update/facture-vente-update.component';
import { FactureVenteRoutingResolveService } from './facture-vente-routing-resolve.service';

const factureVenteRoute: Routes = [
  {
    path: '',
    component: FactureVenteComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: FactureVenteDetailComponent,
    resolve: {
      factureVente: FactureVenteRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: FactureVenteUpdateComponent,
    resolve: {
      factureVente: FactureVenteRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: FactureVenteUpdateComponent,
    resolve: {
      factureVente: FactureVenteRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(factureVenteRoute)],
  exports: [RouterModule],
})
export class FactureVenteRoutingModule {}
