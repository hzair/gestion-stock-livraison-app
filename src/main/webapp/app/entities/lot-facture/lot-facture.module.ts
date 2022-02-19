import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { LotFactureComponent } from './list/lot-facture.component';
import { LotFactureDetailComponent } from './detail/lot-facture-detail.component';
import { LotFactureUpdateComponent } from './update/lot-facture-update.component';
import { LotFactureDeleteDialogComponent } from './delete/lot-facture-delete-dialog.component';
import { LotFactureRoutingModule } from './route/lot-facture-routing.module';

@NgModule({
  imports: [SharedModule, LotFactureRoutingModule],
  declarations: [LotFactureComponent, LotFactureDetailComponent, LotFactureUpdateComponent, LotFactureDeleteDialogComponent],
  entryComponents: [LotFactureDeleteDialogComponent],
})
export class LotFactureModule {}
