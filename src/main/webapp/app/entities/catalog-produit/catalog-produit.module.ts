import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { CatalogProduitComponent } from './list/catalog-produit.component';
import { CatalogProduitDetailComponent } from './detail/catalog-produit-detail.component';
import { CatalogProduitUpdateComponent } from './update/catalog-produit-update.component';
import { CatalogProduitDeleteDialogComponent } from './delete/catalog-produit-delete-dialog.component';
import { CatalogProduitRoutingModule } from './route/catalog-produit-routing.module';

@NgModule({
  imports: [SharedModule, CatalogProduitRoutingModule],
  declarations: [
    CatalogProduitComponent,
    CatalogProduitDetailComponent,
    CatalogProduitUpdateComponent,
    CatalogProduitDeleteDialogComponent,
  ],
  entryComponents: [CatalogProduitDeleteDialogComponent],
})
export class CatalogProduitModule {}
