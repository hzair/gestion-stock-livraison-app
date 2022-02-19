import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { LotCamionComponent } from '../list/lot-camion.component';
import { LotCamionDetailComponent } from '../detail/lot-camion-detail.component';
import { LotCamionUpdateComponent } from '../update/lot-camion-update.component';
import { LotCamionRoutingResolveService } from './lot-camion-routing-resolve.service';

const lotCamionRoute: Routes = [
  {
    path: '',
    component: LotCamionComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: LotCamionDetailComponent,
    resolve: {
      lotCamion: LotCamionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: LotCamionUpdateComponent,
    resolve: {
      lotCamion: LotCamionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: LotCamionUpdateComponent,
    resolve: {
      lotCamion: LotCamionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(lotCamionRoute)],
  exports: [RouterModule],
})
export class LotCamionRoutingModule {}
