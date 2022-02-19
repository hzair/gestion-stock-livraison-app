import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ICatalogTypeProduit } from '../catalog-type-produit.model';
import { CatalogTypeProduitService } from '../service/catalog-type-produit.service';

@Component({
  templateUrl: './catalog-type-produit-delete-dialog.component.html',
})
export class CatalogTypeProduitDeleteDialogComponent {
  catalogTypeProduit?: ICatalogTypeProduit;

  constructor(protected catalogTypeProduitService: CatalogTypeProduitService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.catalogTypeProduitService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
