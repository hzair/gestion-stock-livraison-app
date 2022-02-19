import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { LotFactureService } from '../service/lot-facture.service';
import { ILotFacture, LotFacture } from '../lot-facture.model';
import { ICatalogProduit } from 'app/entities/catalog-produit/catalog-produit.model';
import { CatalogProduitService } from 'app/entities/catalog-produit/service/catalog-produit.service';
import { IFactureAchat } from 'app/entities/facture-achat/facture-achat.model';
import { FactureAchatService } from 'app/entities/facture-achat/service/facture-achat.service';
import { IFactureVente } from 'app/entities/facture-vente/facture-vente.model';
import { FactureVenteService } from 'app/entities/facture-vente/service/facture-vente.service';

import { LotFactureUpdateComponent } from './lot-facture-update.component';

describe('LotFacture Management Update Component', () => {
  let comp: LotFactureUpdateComponent;
  let fixture: ComponentFixture<LotFactureUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let lotFactureService: LotFactureService;
  let catalogProduitService: CatalogProduitService;
  let factureAchatService: FactureAchatService;
  let factureVenteService: FactureVenteService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [LotFactureUpdateComponent],
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
      .overrideTemplate(LotFactureUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(LotFactureUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    lotFactureService = TestBed.inject(LotFactureService);
    catalogProduitService = TestBed.inject(CatalogProduitService);
    factureAchatService = TestBed.inject(FactureAchatService);
    factureVenteService = TestBed.inject(FactureVenteService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call CatalogProduit query and add missing value', () => {
      const lotFacture: ILotFacture = { id: 456 };
      const produit: ICatalogProduit = { id: 71754 };
      lotFacture.produit = produit;

      const catalogProduitCollection: ICatalogProduit[] = [{ id: 35750 }];
      jest.spyOn(catalogProduitService, 'query').mockReturnValue(of(new HttpResponse({ body: catalogProduitCollection })));
      const additionalCatalogProduits = [produit];
      const expectedCollection: ICatalogProduit[] = [...additionalCatalogProduits, ...catalogProduitCollection];
      jest.spyOn(catalogProduitService, 'addCatalogProduitToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ lotFacture });
      comp.ngOnInit();

      expect(catalogProduitService.query).toHaveBeenCalled();
      expect(catalogProduitService.addCatalogProduitToCollectionIfMissing).toHaveBeenCalledWith(
        catalogProduitCollection,
        ...additionalCatalogProduits
      );
      expect(comp.catalogProduitsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call FactureAchat query and add missing value', () => {
      const lotFacture: ILotFacture = { id: 456 };
      const factureAchat: IFactureAchat = { id: 15335 };
      lotFacture.factureAchat = factureAchat;

      const factureAchatCollection: IFactureAchat[] = [{ id: 65022 }];
      jest.spyOn(factureAchatService, 'query').mockReturnValue(of(new HttpResponse({ body: factureAchatCollection })));
      const additionalFactureAchats = [factureAchat];
      const expectedCollection: IFactureAchat[] = [...additionalFactureAchats, ...factureAchatCollection];
      jest.spyOn(factureAchatService, 'addFactureAchatToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ lotFacture });
      comp.ngOnInit();

      expect(factureAchatService.query).toHaveBeenCalled();
      expect(factureAchatService.addFactureAchatToCollectionIfMissing).toHaveBeenCalledWith(
        factureAchatCollection,
        ...additionalFactureAchats
      );
      expect(comp.factureAchatsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call FactureVente query and add missing value', () => {
      const lotFacture: ILotFacture = { id: 456 };
      const factureVente: IFactureVente = { id: 35266 };
      lotFacture.factureVente = factureVente;

      const factureVenteCollection: IFactureVente[] = [{ id: 94630 }];
      jest.spyOn(factureVenteService, 'query').mockReturnValue(of(new HttpResponse({ body: factureVenteCollection })));
      const additionalFactureVentes = [factureVente];
      const expectedCollection: IFactureVente[] = [...additionalFactureVentes, ...factureVenteCollection];
      jest.spyOn(factureVenteService, 'addFactureVenteToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ lotFacture });
      comp.ngOnInit();

      expect(factureVenteService.query).toHaveBeenCalled();
      expect(factureVenteService.addFactureVenteToCollectionIfMissing).toHaveBeenCalledWith(
        factureVenteCollection,
        ...additionalFactureVentes
      );
      expect(comp.factureVentesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const lotFacture: ILotFacture = { id: 456 };
      const produit: ICatalogProduit = { id: 567 };
      lotFacture.produit = produit;
      const factureAchat: IFactureAchat = { id: 88216 };
      lotFacture.factureAchat = factureAchat;
      const factureVente: IFactureVente = { id: 10771 };
      lotFacture.factureVente = factureVente;

      activatedRoute.data = of({ lotFacture });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(lotFacture));
      expect(comp.catalogProduitsSharedCollection).toContain(produit);
      expect(comp.factureAchatsSharedCollection).toContain(factureAchat);
      expect(comp.factureVentesSharedCollection).toContain(factureVente);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<LotFacture>>();
      const lotFacture = { id: 123 };
      jest.spyOn(lotFactureService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ lotFacture });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: lotFacture }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(lotFactureService.update).toHaveBeenCalledWith(lotFacture);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<LotFacture>>();
      const lotFacture = new LotFacture();
      jest.spyOn(lotFactureService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ lotFacture });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: lotFacture }));
      saveSubject.complete();

      // THEN
      expect(lotFactureService.create).toHaveBeenCalledWith(lotFacture);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<LotFacture>>();
      const lotFacture = { id: 123 };
      jest.spyOn(lotFactureService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ lotFacture });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(lotFactureService.update).toHaveBeenCalledWith(lotFacture);
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

    describe('trackFactureAchatById', () => {
      it('Should return tracked FactureAchat primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackFactureAchatById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackFactureVenteById', () => {
      it('Should return tracked FactureVente primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackFactureVenteById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
