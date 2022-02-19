import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { FactureAchatService } from '../service/facture-achat.service';
import { IFactureAchat, FactureAchat } from '../facture-achat.model';
import { IFournisseur } from 'app/entities/fournisseur/fournisseur.model';
import { FournisseurService } from 'app/entities/fournisseur/service/fournisseur.service';

import { FactureAchatUpdateComponent } from './facture-achat-update.component';

describe('FactureAchat Management Update Component', () => {
  let comp: FactureAchatUpdateComponent;
  let fixture: ComponentFixture<FactureAchatUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let factureAchatService: FactureAchatService;
  let fournisseurService: FournisseurService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [FactureAchatUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(FactureAchatUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(FactureAchatUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    factureAchatService = TestBed.inject(FactureAchatService);
    fournisseurService = TestBed.inject(FournisseurService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Fournisseur query and add missing value', () => {
      const factureAchat: IFactureAchat = { id: 456 };
      const fournisseur: IFournisseur = { id: 18470 };
      factureAchat.fournisseur = fournisseur;

      const fournisseurCollection: IFournisseur[] = [{ id: 62101 }];
      jest.spyOn(fournisseurService, 'query').mockReturnValue(of(new HttpResponse({ body: fournisseurCollection })));
      const additionalFournisseurs = [fournisseur];
      const expectedCollection: IFournisseur[] = [...additionalFournisseurs, ...fournisseurCollection];
      jest.spyOn(fournisseurService, 'addFournisseurToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ factureAchat });
      comp.ngOnInit();

      expect(fournisseurService.query).toHaveBeenCalled();
      expect(fournisseurService.addFournisseurToCollectionIfMissing).toHaveBeenCalledWith(fournisseurCollection, ...additionalFournisseurs);
      expect(comp.fournisseursSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const factureAchat: IFactureAchat = { id: 456 };
      const fournisseur: IFournisseur = { id: 10604 };
      factureAchat.fournisseur = fournisseur;

      activatedRoute.data = of({ factureAchat });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(factureAchat));
      expect(comp.fournisseursSharedCollection).toContain(fournisseur);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<FactureAchat>>();
      const factureAchat = { id: 123 };
      jest.spyOn(factureAchatService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ factureAchat });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: factureAchat }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(factureAchatService.update).toHaveBeenCalledWith(factureAchat);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<FactureAchat>>();
      const factureAchat = new FactureAchat();
      jest.spyOn(factureAchatService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ factureAchat });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: factureAchat }));
      saveSubject.complete();

      // THEN
      expect(factureAchatService.create).toHaveBeenCalledWith(factureAchat);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<FactureAchat>>();
      const factureAchat = { id: 123 };
      jest.spyOn(factureAchatService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ factureAchat });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(factureAchatService.update).toHaveBeenCalledWith(factureAchat);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackFournisseurById', () => {
      it('Should return tracked Fournisseur primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackFournisseurById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
