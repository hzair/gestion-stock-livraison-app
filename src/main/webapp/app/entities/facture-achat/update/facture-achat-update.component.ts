import { Component, OnInit, ElementRef } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IFactureAchat, FactureAchat } from '../facture-achat.model';
import { FactureAchatService } from '../service/facture-achat.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IFournisseur } from 'app/entities/fournisseur/fournisseur.model';
import { FournisseurService } from 'app/entities/fournisseur/service/fournisseur.service';

@Component({
  selector: 'jhi-facture-achat-update',
  templateUrl: './facture-achat-update.component.html',
})
export class FactureAchatUpdateComponent implements OnInit {
  isSaving = false;

  fournisseursSharedCollection: IFournisseur[] = [];

  editForm = this.fb.group({
    id: [],
    photo: [],
    photoContentType: [],
    idFonc: [],
    designation: [null, [Validators.required]],
    description: [],
    montant: [null, [Validators.required]],
    date: [],
    payee: [],
    fournisseur: [null, Validators.required],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected factureAchatService: FactureAchatService,
    protected fournisseurService: FournisseurService,
    protected elementRef: ElementRef,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ factureAchat }) => {
      if (factureAchat.id === undefined) {
        const today = dayjs().startOf('day');
        factureAchat.date = today;
      }

      this.updateForm(factureAchat);

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
    const factureAchat = this.createFromForm();
    if (factureAchat.id !== undefined) {
      this.subscribeToSaveResponse(this.factureAchatService.update(factureAchat));
    } else {
      this.subscribeToSaveResponse(this.factureAchatService.create(factureAchat));
    }
  }

  trackFournisseurById(index: number, item: IFournisseur): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFactureAchat>>): void {
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

  protected updateForm(factureAchat: IFactureAchat): void {
    this.editForm.patchValue({
      id: factureAchat.id,
      photo: factureAchat.photo,
      photoContentType: factureAchat.photoContentType,
      idFonc: factureAchat.idFonc,
      designation: factureAchat.designation,
      description: factureAchat.description,
      montant: factureAchat.montant,
      date: factureAchat.date ? factureAchat.date.format(DATE_TIME_FORMAT) : null,
      payee: factureAchat.payee,
      fournisseur: factureAchat.fournisseur,
    });

    this.fournisseursSharedCollection = this.fournisseurService.addFournisseurToCollectionIfMissing(
      this.fournisseursSharedCollection,
      factureAchat.fournisseur
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
  }

  protected createFromForm(): IFactureAchat {
    return {
      ...new FactureAchat(),
      id: this.editForm.get(['id'])!.value,
      photoContentType: this.editForm.get(['photoContentType'])!.value,
      photo: this.editForm.get(['photo'])!.value,
      idFonc: this.editForm.get(['idFonc'])!.value,
      designation: this.editForm.get(['designation'])!.value,
      description: this.editForm.get(['description'])!.value,
      montant: this.editForm.get(['montant'])!.value,
      date: this.editForm.get(['date'])!.value ? dayjs(this.editForm.get(['date'])!.value, DATE_TIME_FORMAT) : undefined,
      payee: this.editForm.get(['payee'])!.value,
      fournisseur: this.editForm.get(['fournisseur'])!.value,
    };
  }
}
