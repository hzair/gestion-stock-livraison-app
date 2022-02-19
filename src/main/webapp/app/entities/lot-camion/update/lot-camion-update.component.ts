import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { ILotCamion, LotCamion } from '../lot-camion.model';
import { LotCamionService } from '../service/lot-camion.service';
import { ICatalogProduit } from 'app/entities/catalog-produit/catalog-produit.model';
import { CatalogProduitService } from 'app/entities/catalog-produit/service/catalog-produit.service';
import { ICamion } from 'app/entities/camion/camion.model';
import { CamionService } from 'app/entities/camion/service/camion.service';

@Component({
  selector: 'jhi-lot-camion-update',
  templateUrl: './lot-camion-update.component.html',
})
export class LotCamionUpdateComponent implements OnInit {
  isSaving = false;

  catalogProduitsSharedCollection: ICatalogProduit[] = [];
  camionsSharedCollection: ICamion[] = [];

  editForm = this.fb.group({
    id: [],
    quantite: [],
    date: [],
    montantTotal: [],
    produit: [null, Validators.required],
    camion: [null, Validators.required],
  });

  constructor(
    protected lotCamionService: LotCamionService,
    protected catalogProduitService: CatalogProduitService,
    protected camionService: CamionService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ lotCamion }) => {
      if (lotCamion.id === undefined) {
        const today = dayjs().startOf('day');
        lotCamion.date = today;
      }

      this.updateForm(lotCamion);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const lotCamion = this.createFromForm();
    if (lotCamion.id !== undefined) {
      this.subscribeToSaveResponse(this.lotCamionService.update(lotCamion));
    } else {
      this.subscribeToSaveResponse(this.lotCamionService.create(lotCamion));
    }
  }

  trackCatalogProduitById(index: number, item: ICatalogProduit): number {
    return item.id!;
  }

  trackCamionById(index: number, item: ICamion): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ILotCamion>>): void {
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

  protected updateForm(lotCamion: ILotCamion): void {
    this.editForm.patchValue({
      id: lotCamion.id,
      quantite: lotCamion.quantite,
      date: lotCamion.date ? lotCamion.date.format(DATE_TIME_FORMAT) : null,
      montantTotal: lotCamion.montantTotal,
      produit: lotCamion.produit,
      camion: lotCamion.camion,
    });

    this.catalogProduitsSharedCollection = this.catalogProduitService.addCatalogProduitToCollectionIfMissing(
      this.catalogProduitsSharedCollection,
      lotCamion.produit
    );
    this.camionsSharedCollection = this.camionService.addCamionToCollectionIfMissing(this.camionsSharedCollection, lotCamion.camion);
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

    this.camionService
      .query()
      .pipe(map((res: HttpResponse<ICamion[]>) => res.body ?? []))
      .pipe(map((camions: ICamion[]) => this.camionService.addCamionToCollectionIfMissing(camions, this.editForm.get('camion')!.value)))
      .subscribe((camions: ICamion[]) => (this.camionsSharedCollection = camions));
  }

  protected createFromForm(): ILotCamion {
    return {
      ...new LotCamion(),
      id: this.editForm.get(['id'])!.value,
      quantite: this.editForm.get(['quantite'])!.value,
      date: this.editForm.get(['date'])!.value ? dayjs(this.editForm.get(['date'])!.value, DATE_TIME_FORMAT) : undefined,
      montantTotal: this.editForm.get(['montantTotal'])!.value,
      produit: this.editForm.get(['produit'])!.value,
      camion: this.editForm.get(['camion'])!.value,
    };
  }
}
