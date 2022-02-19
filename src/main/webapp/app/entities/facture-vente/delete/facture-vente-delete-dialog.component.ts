import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IFactureVente } from '../facture-vente.model';
import { FactureVenteService } from '../service/facture-vente.service';

@Component({
  templateUrl: './facture-vente-delete-dialog.component.html',
})
export class FactureVenteDeleteDialogComponent {
  factureVente?: IFactureVente;

  constructor(protected factureVenteService: FactureVenteService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.factureVenteService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
