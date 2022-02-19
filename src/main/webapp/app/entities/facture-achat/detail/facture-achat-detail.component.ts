import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IFactureAchat } from '../facture-achat.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-facture-achat-detail',
  templateUrl: './facture-achat-detail.component.html',
})
export class FactureAchatDetailComponent implements OnInit {
  factureAchat: IFactureAchat | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ factureAchat }) => {
      this.factureAchat = factureAchat;
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
