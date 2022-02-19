import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IFactureVente } from '../facture-vente.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-facture-vente-detail',
  templateUrl: './facture-vente-detail.component.html',
})
export class FactureVenteDetailComponent implements OnInit {
  factureVente: IFactureVente | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ factureVente }) => {
      this.factureVente = factureVente;
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  previousState(): void {
    window.history.back();
  }
}
