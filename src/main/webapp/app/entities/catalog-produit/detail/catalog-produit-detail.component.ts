import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICatalogProduit } from '../catalog-produit.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-catalog-produit-detail',
  templateUrl: './catalog-produit-detail.component.html',
})
export class CatalogProduitDetailComponent implements OnInit {
  catalogProduit: ICatalogProduit | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ catalogProduit }) => {
      this.catalogProduit = catalogProduit;
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
