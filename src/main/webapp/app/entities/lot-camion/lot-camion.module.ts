import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { LotCamionComponent } from './list/lot-camion.component';
import { LotCamionDetailComponent } from './detail/lot-camion-detail.component';
import { LotCamionUpdateComponent } from './update/lot-camion-update.component';
import { LotCamionDeleteDialogComponent } from './delete/lot-camion-delete-dialog.component';
import { LotCamionRoutingModule } from './route/lot-camion-routing.module';

@NgModule({
  imports: [SharedModule, LotCamionRoutingModule],
  declarations: [LotCamionComponent, LotCamionDetailComponent, LotCamionUpdateComponent, LotCamionDeleteDialogComponent],
  entryComponents: [LotCamionDeleteDialogComponent],
})
export class LotCamionModule {}
