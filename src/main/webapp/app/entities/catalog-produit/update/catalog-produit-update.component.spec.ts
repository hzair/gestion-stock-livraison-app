import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { CatalogProduitService } from '../service/catalog-produit.service';
import { ICatalogProduit, CatalogProduit } from '../catalog-produit.model';
import { IFournisseur } from 'app/entities/fournisseur/fournisseur.model';
import { FournisseurService } from 'app/entities/fournisseur/service/fournisseur.service';
import { ICatalogTypeProduit } from 'app/entities/catalog-type-produit/catalog-type-produit.model';
import { CatalogTypeProduitService } from 'app/entities/catalog-type-produit/service/catalog-type-produit.service';

import { CatalogProduitUpdateComponent } from './catalog-produit-update.component';

describe('CatalogProduit Management Update Component', () => {
  let comp: CatalogProduitUpdateComponent;
  let fixture: ComponentFixture<CatalogProduitUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let catalogProduitService: CatalogProduitService;
  let fournisseurService: FournisseurService;
  let catalogTypeProduitService: CatalogTypeProduitService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [CatalogProduitUpdateComponent],
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
      .overrideTemplate(CatalogProduitUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CatalogProduitUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    catalogProduitService = TestBed.inject(CatalogProduitService);
    fournisseurService = TestBed.inject(FournisseurService);
    catalogTypeProduitService = TestBed.inject(CatalogTypeProduitService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Fournisseur query and add missing value', () => {
      const catalogProduit: ICatalogProduit = { id: 456 };
      const fournisseur: IFournisseur = { id: 18044 };
      catalogProduit.fournisseur = fournisseur;

      const fournisseurCollection: IFournisseur[] = [{ id: 80542 }];
      jest.spyOn(fournisseurService, 'query').mockReturnValue(of(new HttpResponse({ body: fournisseurCollection })));
      const additionalFournisseurs = [fournisseur];
      const expectedCollection: IFournisseur[] = [...additionalFournisseurs, ...fournisseurCollection];
      jest.spyOn(fournisseurService, 'addFournisseurToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ catalogProduit });
      comp.ngOnInit();

      expect(fournisseurService.query).toHaveBeenCalled();
      expect(fournisseurService.addFournisseurToCollectionIfMissing).toHaveBeenCalledWith(fournisseurCollection, ...additionalFournisseurs);
      expect(comp.fournisseursSharedCollection).toEqual(expectedCollection);
    });

    it('Should call CatalogTypeProduit query and add missing value', () => {
      const catalogProduit: ICatalogProduit = { id: 456 };
      const type: ICatalogTypeProduit = { id: 18232 };
      catalogProduit.type = type;

      const catalogTypeProduitCollection: ICatalogTypeProduit[] = [{ id: 45955 }];
      jest.spyOn(catalogTypeProduitService, 'query').mockReturnValue(of(new HttpResponse({ body: catalogTypeProduitCollection })));
      const additionalCatalogTypeProduits = [type];
      const expectedCollection: ICatalogTypeProduit[] = [...additionalCatalogTypeProduits, ...catalogTypeProduitCollection];
      jest.spyOn(catalogTypeProduitService, 'addCatalogTypeProduitToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ catalogProduit });
      comp.ngOnInit();

      expect(catalogTypeProduitService.query).toHaveBeenCalled();
      expect(catalogTypeProduitService.addCatalogTypeProduitToCollectionIfMissing).toHaveBeenCalledWith(
        catalogTypeProduitCollection,
        ...additionalCatalogTypeProduits
      );
      expect(comp.catalogTypeProduitsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const catalogProduit: ICatalogProduit = { id: 456 };
      const fournisseur: IFournisseur = { id: 24770 };
      catalogProduit.fournisseur = fournisseur;
      const type: ICatalogTypeProduit = { id: 71151 };
      catalogProduit.type = type;

      activatedRoute.data = of({ catalogProduit });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(catalogProduit));
      expect(comp.fournisseursSharedCollection).toContain(fournisseur);
      expect(comp.catalogTypeProduitsSharedCollection).toContain(type);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CatalogProduit>>();
      const catalogProduit = { id: 123 };
      jest.spyOn(catalogProduitService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ catalogProduit });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: catalogProduit }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(catalogProduitService.update).toHaveBeenCalledWith(catalogProduit);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CatalogProduit>>();
      const catalogProduit = new CatalogProduit();
      jest.spyOn(catalogProduitService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ catalogProduit });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: catalogProduit }));
      saveSubject.complete();

      // THEN
      expect(catalogProduitService.create).toHaveBeenCalledWith(catalogProduit);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CatalogProduit>>();
      const catalogProduit = { id: 123 };
      jest.spyOn(catalogProduitService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ catalogProduit });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(catalogProduitService.update).toHaveBeenCalledWith(catalogProduit);
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

    describe('trackCatalogTypeProduitById', () => {
      it('Should return tracked CatalogTypeProduit primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackCatalogTypeProduitById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
