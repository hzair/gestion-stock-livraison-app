import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { FactureAchatComponent } from '../list/facture-achat.component';
import { FactureAchatDetailComponent } from '../detail/facture-achat-detail.component';
import { FactureAchatUpdateComponent } from '../update/facture-achat-update.component';
import { FactureAchatRoutingResolveService } from './facture-achat-routing-resolve.service';

const factureAchatRoute: Routes = [
  {
    path: '',
    component: FactureAchatComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: FactureAchatDetailComponent,
    resolve: {
      factureAchat: FactureAchatRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: FactureAchatUpdateComponent,
    resolve: {
      factureAchat: FactureAchatRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: FactureAchatUpdateComponent,
    resolve: {
      factureAchat: FactureAchatRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(factureAchatRoute)],
  exports: [RouterModule],
})
export class FactureAchatRoutingModule {}
