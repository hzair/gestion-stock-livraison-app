import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IFactureAchat } from '../facture-achat.model';
import { FactureAchatService } from '../service/facture-achat.service';

@Component({
  templateUrl: './facture-achat-delete-dialog.component.html',
})
export class FactureAchatDeleteDialogComponent {
  factureAchat?: IFactureAchat;

  constructor(protected factureAchatService: FactureAchatService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.factureAchatService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
