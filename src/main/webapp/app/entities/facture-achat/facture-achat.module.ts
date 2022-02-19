import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { FactureAchatComponent } from './list/facture-achat.component';
import { FactureAchatDetailComponent } from './detail/facture-achat-detail.component';
import { FactureAchatUpdateComponent } from './update/facture-achat-update.component';
import { FactureAchatDeleteDialogComponent } from './delete/facture-achat-delete-dialog.component';
import { FactureAchatRoutingModule } from './route/facture-achat-routing.module';

@NgModule({
  imports: [SharedModule, FactureAchatRoutingModule],
  declarations: [FactureAchatComponent, FactureAchatDetailComponent, FactureAchatUpdateComponent, FactureAchatDeleteDialogComponent],
  entryComponents: [FactureAchatDeleteDialogComponent],
})
export class FactureAchatModule {}
