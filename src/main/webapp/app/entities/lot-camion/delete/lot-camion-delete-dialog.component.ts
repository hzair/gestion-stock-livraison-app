import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ILotCamion } from '../lot-camion.model';
import { LotCamionService } from '../service/lot-camion.service';

@Component({
  templateUrl: './lot-camion-delete-dialog.component.html',
})
export class LotCamionDeleteDialogComponent {
  lotCamion?: ILotCamion;

  constructor(protected lotCamionService: LotCamionService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.lotCamionService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
