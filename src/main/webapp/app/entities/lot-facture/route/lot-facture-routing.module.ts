import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { LotFactureComponent } from '../list/lot-facture.component';
import { LotFactureDetailComponent } from '../detail/lot-facture-detail.component';
import { LotFactureUpdateComponent } from '../update/lot-facture-update.component';
import { LotFactureRoutingResolveService } from './lot-facture-routing-resolve.service';

const lotFactureRoute: Routes = [
  {
    path: '',
    component: LotFactureComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: LotFactureDetailComponent,
    resolve: {
      lotFacture: LotFactureRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: LotFactureUpdateComponent,
    resolve: {
      lotFacture: LotFactureRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: LotFactureUpdateComponent,
    resolve: {
      lotFacture: LotFactureRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(lotFactureRoute)],
  exports: [RouterModule],
})
export class LotFactureRoutingModule {}
