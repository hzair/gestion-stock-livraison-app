import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ILotCamion } from '../lot-camion.model';

@Component({
  selector: 'jhi-lot-camion-detail',
  templateUrl: './lot-camion-detail.component.html',
})
export class LotCamionDetailComponent implements OnInit {
  lotCamion: ILotCamion | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ lotCamion }) => {
      this.lotCamion = lotCamion;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
