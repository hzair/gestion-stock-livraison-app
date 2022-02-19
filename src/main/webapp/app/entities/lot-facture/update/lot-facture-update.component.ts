import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { ILotFacture, LotFacture } from '../lot-facture.model';
import { LotFactureService } from '../service/lot-facture.service';
import { ICatalogProduit } from 'app/entities/catalog-produit/catalog-produit.model';
import { CatalogProduitService } from 'app/entities/catalog-produit/service/catalog-produit.service';
import { IFactureAchat } from 'app/entities/facture-achat/facture-achat.model';
import { FactureAchatService } from 'app/entities/facture-achat/service/facture-achat.service';
import { IFactureVente } from 'app/entities/facture-vente/facture-vente.model';
import { FactureVenteService } from 'app/entities/facture-vente/service/facture-vente.service';

@Component({
  selector: 'jhi-lot-facture-update',
  templateUrl: './lot-facture-update.component.html',
})
export class LotFactureUpdateComponent implements OnInit {
  isSaving = false;

  catalogProduitsSharedCollection: ICatalogProduit[] = [];
  factureAchatsSharedCollection: IFactureAchat[] = [];
  factureVentesSharedCollection: IFactureVente[] = [];

  editForm = this.fb.group({
    id: [],
    quantite: [],
    date: [],
    montantTotal: [],
    produit: [null, Validators.required],
    factureAchat: [],
    factureVente: [],
  });

  constructor(
    protected lotFactureService: LotFactureService,
    protected catalogProduitService: CatalogProduitService,
    protected factureAchatService: FactureAchatService,
    protected factureVenteService: FactureVenteService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ lotFacture }) => {
      if (lotFacture.id === undefined) {
        const today = dayjs().startOf('day');
        lotFacture.date = today;
      }

      this.updateForm(lotFacture);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const lotFacture = this.createFromForm();
    if (lotFacture.id !== undefined) {
      this.subscribeToSaveResponse(this.lotFactureService.update(lotFacture));
    } else {
      this.subscribeToSaveResponse(this.lotFactureService.create(lotFacture));
    }
  }

  trackCatalogProduitById(index: number, item: ICatalogProduit): number {
    return item.id!;
  }

  trackFactureAchatById(index: number, item: IFactureAchat): number {
    return item.id!;
  }

  trackFactureVenteById(index: number, item: IFactureVente): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ILotFacture>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(lotFacture: ILotFacture): void {
    this.editForm.patchValue({
      id: lotFacture.id,
      quantite: lotFacture.quantite,
      date: lotFacture.date ? lotFacture.date.format(DATE_TIME_FORMAT) : null,
      montantTotal: lotFacture.montantTotal,
      produit: lotFacture.produit,
      factureAchat: lotFacture.factureAchat,
      factureVente: lotFacture.factureVente,
    });

    this.catalogProduitsSharedCollection = this.catalogProduitService.addCatalogProduitToCollectionIfMissing(
      this.catalogProduitsSharedCollection,
      lotFacture.produit
    );
    this.factureAchatsSharedCollection = this.factureAchatService.addFactureAchatToCollectionIfMissing(
      this.factureAchatsSharedCollection,
      lotFacture.factureAchat
    );
    this.factureVentesSharedCollection = this.factureVenteService.addFactureVenteToCollectionIfMissing(
      this.factureVentesSharedCollection,
      lotFacture.factureVente
    );
  }

  protected loadRelationshipsOptions(): void {
    this.catalogProduitService
      .query()
      .pipe(map((res: HttpResponse<ICatalogProduit[]>) => res.body ?? []))
      .pipe(
        map((catalogProduits: ICatalogProduit[]) =>
          this.catalogProduitService.addCatalogProduitToCollectionIfMissing(catalogProduits, this.editForm.get('produit')!.value)
        )
      )
      .subscribe((catalogProduits: ICatalogProduit[]) => (this.catalogProduitsSharedCollection = catalogProduits));

    this.factureAchatService
      .query()
      .pipe(map((res: HttpResponse<IFactureAchat[]>) => res.body ?? []))
      .pipe(
        map((factureAchats: IFactureAchat[]) =>
          this.factureAchatService.addFactureAchatToCollectionIfMissing(factureAchats, this.editForm.get('factureAchat')!.value)
        )
      )
      .subscribe((factureAchats: IFactureAchat[]) => (this.factureAchatsSharedCollection = factureAchats));

    this.factureVenteService
      .query()
      .pipe(map((res: HttpResponse<IFactureVente[]>) => res.body ?? []))
      .pipe(
        map((factureVentes: IFactureVente[]) =>
          this.factureVenteService.addFactureVenteToCollectionIfMissing(factureVentes, this.editForm.get('factureVente')!.value)
        )
      )
      .subscribe((factureVentes: IFactureVente[]) => (this.factureVentesSharedCollection = factureVentes));
  }

  protected createFromForm(): ILotFacture {
    return {
      ...new LotFacture(),
      id: this.editForm.get(['id'])!.value,
      quantite: this.editForm.get(['quantite'])!.value,
      date: this.editForm.get(['date'])!.value ? dayjs(this.editForm.get(['date'])!.value, DATE_TIME_FORMAT) : undefined,
      montantTotal: this.editForm.get(['montantTotal'])!.value,
      produit: this.editForm.get(['produit'])!.value,
      factureAchat: this.editForm.get(['factureAchat'])!.value,
      factureVente: this.editForm.get(['factureVente'])!.value,
    };
  }
}
