import { Component, OnInit, ElementRef } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { ICatalogProduit, CatalogProduit } from '../catalog-produit.model';
import { CatalogProduitService } from '../service/catalog-produit.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IFournisseur } from 'app/entities/fournisseur/fournisseur.model';
import { FournisseurService } from 'app/entities/fournisseur/service/fournisseur.service';
import { ICatalogTypeProduit } from 'app/entities/catalog-type-produit/catalog-type-produit.model';
import { CatalogTypeProduitService } from 'app/entities/catalog-type-produit/service/catalog-type-produit.service';

@Component({
  selector: 'jhi-catalog-produit-update',
  templateUrl: './catalog-produit-update.component.html',
})
export class CatalogProduitUpdateComponent implements OnInit {
  isSaving = false;

  fournisseursSharedCollection: IFournisseur[] = [];
  catalogTypeProduitsSharedCollection: ICatalogTypeProduit[] = [];

  editForm = this.fb.group({
    id: [],
    image: [],
    imageContentType: [],
    idFonc: [],
    designation: [null, [Validators.required]],
    description: [],
    quantite: [null, [Validators.required]],
    prixAchatUnit: [null, [Validators.required]],
    prixVenteUnit: [null, [Validators.required]],
    prixVenteGros: [],
    date: [],
    fournisseur: [null, Validators.required],
    type: [null, Validators.required],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected catalogProduitService: CatalogProduitService,
    protected fournisseurService: FournisseurService,
    protected catalogTypeProduitService: CatalogTypeProduitService,
    protected elementRef: ElementRef,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ catalogProduit }) => {
      if (catalogProduit.id === undefined) {
        const today = dayjs().startOf('day');
        catalogProduit.date = today;
      }

      this.updateForm(catalogProduit);

      this.loadRelationshipsOptions();
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe({
      error: (err: FileLoadError) =>
        this.eventManager.broadcast(
          new EventWithContent<AlertError>('gestionStockLivraisonApp.error', { ...err, key: 'error.file.' + err.key })
        ),
    });
  }

  clearInputImage(field: string, fieldContentType: string, idInput: string): void {
    this.editForm.patchValue({
      [field]: null,
      [fieldContentType]: null,
    });
    if (idInput && this.elementRef.nativeElement.querySelector('#' + idInput)) {
      this.elementRef.nativeElement.querySelector('#' + idInput).value = null;
    }
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const catalogProduit = this.createFromForm();
    if (catalogProduit.id !== undefined) {
      this.subscribeToSaveResponse(this.catalogProduitService.update(catalogProduit));
    } else {
      this.subscribeToSaveResponse(this.catalogProduitService.create(catalogProduit));
    }
  }

  trackFournisseurById(index: number, item: IFournisseur): number {
    return item.id!;
  }

  trackCatalogTypeProduitById(index: number, item: ICatalogTypeProduit): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICatalogProduit>>): void {
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

  protected updateForm(catalogProduit: ICatalogProduit): void {
    this.editForm.patchValue({
      id: catalogProduit.id,
      image: catalogProduit.image,
      imageContentType: catalogProduit.imageContentType,
      idFonc: catalogProduit.idFonc,
      designation: catalogProduit.designation,
      description: catalogProduit.description,
      quantite: catalogProduit.quantite,
      prixAchatUnit: catalogProduit.prixAchatUnit,
      prixVenteUnit: catalogProduit.prixVenteUnit,
      prixVenteGros: catalogProduit.prixVenteGros,
      date: catalogProduit.date ? catalogProduit.date.format(DATE_TIME_FORMAT) : null,
      fournisseur: catalogProduit.fournisseur,
      type: catalogProduit.type,
    });

    this.fournisseursSharedCollection = this.fournisseurService.addFournisseurToCollectionIfMissing(
      this.fournisseursSharedCollection,
      catalogProduit.fournisseur
    );
    this.catalogTypeProduitsSharedCollection = this.catalogTypeProduitService.addCatalogTypeProduitToCollectionIfMissing(
      this.catalogTypeProduitsSharedCollection,
      catalogProduit.type
    );
  }

  protected loadRelationshipsOptions(): void {
    this.fournisseurService
      .query()
      .pipe(map((res: HttpResponse<IFournisseur[]>) => res.body ?? []))
      .pipe(
        map((fournisseurs: IFournisseur[]) =>
          this.fournisseurService.addFournisseurToCollectionIfMissing(fournisseurs, this.editForm.get('fournisseur')!.value)
        )
      )
      .subscribe((fournisseurs: IFournisseur[]) => (this.fournisseursSharedCollection = fournisseurs));

    this.catalogTypeProduitService
      .query()
      .pipe(map((res: HttpResponse<ICatalogTypeProduit[]>) => res.body ?? []))
      .pipe(
        map((catalogTypeProduits: ICatalogTypeProduit[]) =>
          this.catalogTypeProduitService.addCatalogTypeProduitToCollectionIfMissing(catalogTypeProduits, this.editForm.get('type')!.value)
        )
      )
      .subscribe((catalogTypeProduits: ICatalogTypeProduit[]) => (this.catalogTypeProduitsSharedCollection = catalogTypeProduits));
  }

  protected createFromForm(): ICatalogProduit {
    return {
      ...new CatalogProduit(),
      id: this.editForm.get(['id'])!.value,
      imageContentType: this.editForm.get(['imageContentType'])!.value,
      image: this.editForm.get(['image'])!.value,
      idFonc: this.editForm.get(['idFonc'])!.value,
      designation: this.editForm.get(['designation'])!.value,
      description: this.editForm.get(['description'])!.value,
      quantite: this.editForm.get(['quantite'])!.value,
      prixAchatUnit: this.editForm.get(['prixAchatUnit'])!.value,
      prixVenteUnit: this.editForm.get(['prixVenteUnit'])!.value,
      prixVenteGros: this.editForm.get(['prixVenteGros'])!.value,
      date: this.editForm.get(['date'])!.value ? dayjs(this.editForm.get(['date'])!.value, DATE_TIME_FORMAT) : undefined,
      fournisseur: this.editForm.get(['fournisseur'])!.value,
      type: this.editForm.get(['type'])!.value,
    };
  }
}
