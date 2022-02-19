import { Component, OnInit, ElementRef } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { ICatalogTypeProduit, CatalogTypeProduit } from '../catalog-type-produit.model';
import { CatalogTypeProduitService } from '../service/catalog-type-produit.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-catalog-type-produit-update',
  templateUrl: './catalog-type-produit-update.component.html',
})
export class CatalogTypeProduitUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    image: [],
    imageContentType: [],
    idFonc: [],
    designation: [null, [Validators.required]],
    description: [],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected catalogTypeProduitService: CatalogTypeProduitService,
    protected elementRef: ElementRef,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ catalogTypeProduit }) => {
      this.updateForm(catalogTypeProduit);
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
    const catalogTypeProduit = this.createFromForm();
    if (catalogTypeProduit.id !== undefined) {
      this.subscribeToSaveResponse(this.catalogTypeProduitService.update(catalogTypeProduit));
    } else {
      this.subscribeToSaveResponse(this.catalogTypeProduitService.create(catalogTypeProduit));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICatalogTypeProduit>>): void {
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

  protected updateForm(catalogTypeProduit: ICatalogTypeProduit): void {
    this.editForm.patchValue({
      id: catalogTypeProduit.id,
      image: catalogTypeProduit.image,
      imageContentType: catalogTypeProduit.imageContentType,
      idFonc: catalogTypeProduit.idFonc,
      designation: catalogTypeProduit.designation,
      description: catalogTypeProduit.description,
    });
  }

  protected createFromForm(): ICatalogTypeProduit {
    return {
      ...new CatalogTypeProduit(),
      id: this.editForm.get(['id'])!.value,
      imageContentType: this.editForm.get(['imageContentType'])!.value,
      image: this.editForm.get(['image'])!.value,
      idFonc: this.editForm.get(['idFonc'])!.value,
      designation: this.editForm.get(['designation'])!.value,
      description: this.editForm.get(['description'])!.value,
    };
  }
}
