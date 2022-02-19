import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ILotFacture } from '../lot-facture.model';
import { LotFactureService } from '../service/lot-facture.service';

@Component({
  templateUrl: './lot-facture-delete-dialog.component.html',
})
export class LotFactureDeleteDialogComponent {
  lotFacture?: ILotFacture;

  constructor(protected lotFactureService: LotFactureService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.lotFactureService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
