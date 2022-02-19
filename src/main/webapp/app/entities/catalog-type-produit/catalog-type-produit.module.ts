import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { CatalogTypeProduitComponent } from './list/catalog-type-produit.component';
import { CatalogTypeProduitDetailComponent } from './detail/catalog-type-produit-detail.component';
import { CatalogTypeProduitUpdateComponent } from './update/catalog-type-produit-update.component';
import { CatalogTypeProduitDeleteDialogComponent } from './delete/catalog-type-produit-delete-dialog.component';
import { CatalogTypeProduitRoutingModule } from './route/catalog-type-produit-routing.module';

@NgModule({
  imports: [SharedModule, CatalogTypeProduitRoutingModule],
  declarations: [
    CatalogTypeProduitComponent,
    CatalogTypeProduitDetailComponent,
    CatalogTypeProduitUpdateComponent,
    CatalogTypeProduitDeleteDialogComponent,
  ],
  entryComponents: [CatalogTypeProduitDeleteDialogComponent],
})
export class CatalogTypeProduitModule {}
