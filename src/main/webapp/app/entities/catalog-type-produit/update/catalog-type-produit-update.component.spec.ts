import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { CatalogTypeProduitService } from '../service/catalog-type-produit.service';
import { ICatalogTypeProduit, CatalogTypeProduit } from '../catalog-type-produit.model';

import { CatalogTypeProduitUpdateComponent } from './catalog-type-produit-update.component';

describe('CatalogTypeProduit Management Update Component', () => {
  let comp: CatalogTypeProduitUpdateComponent;
  let fixture: ComponentFixture<CatalogTypeProduitUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let catalogTypeProduitService: CatalogTypeProduitService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [CatalogTypeProduitUpdateComponent],
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
      .overrideTemplate(CatalogTypeProduitUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CatalogTypeProduitUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    catalogTypeProduitService = TestBed.inject(CatalogTypeProduitService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const catalogTypeProduit: ICatalogTypeProduit = { id: 456 };

      activatedRoute.data = of({ catalogTypeProduit });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(catalogTypeProduit));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CatalogTypeProduit>>();
      const catalogTypeProduit = { id: 123 };
      jest.spyOn(catalogTypeProduitService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ catalogTypeProduit });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: catalogTypeProduit }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(catalogTypeProduitService.update).toHaveBeenCalledWith(catalogTypeProduit);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CatalogTypeProduit>>();
      const catalogTypeProduit = new CatalogTypeProduit();
      jest.spyOn(catalogTypeProduitService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ catalogTypeProduit });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: catalogTypeProduit }));
      saveSubject.complete();

      // THEN
      expect(catalogTypeProduitService.create).toHaveBeenCalledWith(catalogTypeProduit);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CatalogTypeProduit>>();
      const catalogTypeProduit = { id: 123 };
      jest.spyOn(catalogTypeProduitService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ catalogTypeProduit });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(catalogTypeProduitService.update).toHaveBeenCalledWith(catalogTypeProduit);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
