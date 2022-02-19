import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ICatalogProduit } from '../catalog-produit.model';
import { CatalogProduitService } from '../service/catalog-produit.service';

@Component({
  templateUrl: './catalog-produit-delete-dialog.component.html',
})
export class CatalogProduitDeleteDialogComponent {
  catalogProduit?: ICatalogProduit;

  constructor(protected catalogProduitService: CatalogProduitService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.catalogProduitService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
