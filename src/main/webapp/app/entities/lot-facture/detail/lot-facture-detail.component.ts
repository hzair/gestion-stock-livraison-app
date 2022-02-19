import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ILotFacture } from '../lot-facture.model';

@Component({
  selector: 'jhi-lot-facture-detail',
  templateUrl: './lot-facture-detail.component.html',
})
export class LotFactureDetailComponent implements OnInit {
  lotFacture: ILotFacture | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ lotFacture }) => {
      this.lotFacture = lotFacture;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
