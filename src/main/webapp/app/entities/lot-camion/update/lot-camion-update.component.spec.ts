import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { LotCamionService } from '../service/lot-camion.service';
import { ILotCamion, LotCamion } from '../lot-camion.model';
import { ICatalogProduit } from 'app/entities/catalog-produit/catalog-produit.model';
import { CatalogProduitService } from 'app/entities/catalog-produit/service/catalog-produit.service';
import { ICamion } from 'app/entities/camion/camion.model';
import { CamionService } from 'app/entities/camion/service/camion.service';

import { LotCamionUpdateComponent } from './lot-camion-update.component';

describe('LotCamion Management Update Component', () => {
  let comp: LotCamionUpdateComponent;
  let fixture: ComponentFixture<LotCamionUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let lotCamionService: LotCamionService;
  let catalogProduitService: CatalogProduitService;
  let camionService: CamionService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [LotCamionUpdateComponent],
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
      .overrideTemplate(LotCamionUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(LotCamionUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    lotCamionService = TestBed.inject(LotCamionService);
    catalogProduitService = TestBed.inject(CatalogProduitService);
    camionService = TestBed.inject(CamionService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call CatalogProduit query and add missing value', () => {
      const lotCamion: ILotCamion = { id: 456 };
      const produit: ICatalogProduit = { id: 31562 };
      lotCamion.produit = produit;

      const catalogProduitCollection: ICatalogProduit[] = [{ id: 15617 }];
      jest.spyOn(catalogProduitService, 'query').mockReturnValue(of(new HttpResponse({ body: catalogProduitCollection })));
      const additionalCatalogProduits = [produit];
      const expectedCollection: ICatalogProduit[] = [...additionalCatalogProduits, ...catalogProduitCollection];
      jest.spyOn(catalogProduitService, 'addCatalogProduitToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ lotCamion });
      comp.ngOnInit();

      expect(catalogProduitService.query).toHaveBeenCalled();
      expect(catalogProduitService.addCatalogProduitToCollectionIfMissing).toHaveBeenCalledWith(
        catalogProduitCollection,
        ...additionalCatalogProduits
      );
      expect(comp.catalogProduitsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Camion query and add missing value', () => {
      const lotCamion: ILotCamion = { id: 456 };
      const camion: ICamion = { id: 29072 };
      lotCamion.camion = camion;

      const camionCollection: ICamion[] = [{ id: 71676 }];
      jest.spyOn(camionService, 'query').mockReturnValue(of(new HttpResponse({ body: camionCollection })));
      const additionalCamions = [camion];
      const expectedCollection: ICamion[] = [...additionalCamions, ...camionCollection];
      jest.spyOn(camionService, 'addCamionToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ lotCamion });
      comp.ngOnInit();

      expect(camionService.query).toHaveBeenCalled();
      expect(camionService.addCamionToCollectionIfMissing).toHaveBeenCalledWith(camionCollection, ...additionalCamions);
      expect(comp.camionsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const lotCamion: ILotCamion = { id: 456 };
      const produit: ICatalogProduit = { id: 63772 };
      lotCamion.produit = produit;
      const camion: ICamion = { id: 23302 };
      lotCamion.camion = camion;

      activatedRoute.data = of({ lotCamion });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(lotCamion));
      expect(comp.catalogProduitsSharedCollection).toContain(produit);
      expect(comp.camionsSharedCollection).toContain(camion);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<LotCamion>>();
      const lotCamion = { id: 123 };
      jest.spyOn(lotCamionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ lotCamion });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: lotCamion }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(lotCamionService.update).toHaveBeenCalledWith(lotCamion);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<LotCamion>>();
      const lotCamion = new LotCamion();
      jest.spyOn(lotCamionService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ lotCamion });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: lotCamion }));
      saveSubject.complete();

      // THEN
      expect(lotCamionService.create).toHaveBeenCalledWith(lotCamion);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<LotCamion>>();
      const lotCamion = { id: 123 };
      jest.spyOn(lotCamionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ lotCamion });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(lotCamionService.update).toHaveBeenCalledWith(lotCamion);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackCatalogProduitById', () => {
      it('Should return tracked CatalogProduit primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackCatalogProduitById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackCamionById', () => {
      it('Should return tracked Camion primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackCamionById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
