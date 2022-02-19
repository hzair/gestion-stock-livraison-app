import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { FactureVenteComponent } from './list/facture-vente.component';
import { FactureVenteDetailComponent } from './detail/facture-vente-detail.component';
import { FactureVenteUpdateComponent } from './update/facture-vente-update.component';
import { FactureVenteDeleteDialogComponent } from './delete/facture-vente-delete-dialog.component';
import { FactureVenteRoutingModule } from './route/facture-vente-routing.module';

@NgModule({
  imports: [SharedModule, FactureVenteRoutingModule],
  declarations: [FactureVenteComponent, FactureVenteDetailComponent, FactureVenteUpdateComponent, FactureVenteDeleteDialogComponent],
  entryComponents: [FactureVenteDeleteDialogComponent],
})
export class FactureVenteModule {}
