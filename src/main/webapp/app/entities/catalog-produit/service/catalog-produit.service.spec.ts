import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ICatalogProduit, CatalogProduit } from '../catalog-produit.model';

import { CatalogProduitService } from './catalog-produit.service';

describe('CatalogProduit Service', () => {
  let service: CatalogProduitService;
  let httpMock: HttpTestingController;
  let elemDefault: ICatalogProduit;
  let expectedResult: ICatalogProduit | ICatalogProduit[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(CatalogProduitService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      imageContentType: 'image/png',
      image: 'AAAAAAA',
      idFonc: 'AAAAAAA',
      designation: 'AAAAAAA',
      description: 'AAAAAAA',
      quantite: 0,
      prixAchatUnit: 0,
      prixVenteUnit: 0,
      prixVenteGros: 0,
      date: currentDate,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          date: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a CatalogProduit', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          date: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          date: currentDate,
        },
        returnedFromService
      );

      service.create(new CatalogProduit()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a CatalogProduit', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          image: 'BBBBBB',
          idFonc: 'BBBBBB',
          designation: 'BBBBBB',
          description: 'BBBBBB',
          quantite: 1,
          prixAchatUnit: 1,
          prixVenteUnit: 1,
          prixVenteGros: 1,
          date: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          date: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a CatalogProduit', () => {
      const patchObject = Object.assign(
        {
          image: 'BBBBBB',
          idFonc: 'BBBBBB',
          description: 'BBBBBB',
          quantite: 1,
          prixAchatUnit: 1,
        },
        new CatalogProduit()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          date: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of CatalogProduit', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          image: 'BBBBBB',
          idFonc: 'BBBBBB',
          designation: 'BBBBBB',
          description: 'BBBBBB',
          quantite: 1,
          prixAchatUnit: 1,
          prixVenteUnit: 1,
          prixVenteGros: 1,
          date: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          date: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a CatalogProduit', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addCatalogProduitToCollectionIfMissing', () => {
      it('should add a CatalogProduit to an empty array', () => {
        const catalogProduit: ICatalogProduit = { id: 123 };
        expectedResult = service.addCatalogProduitToCollectionIfMissing([], catalogProduit);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(catalogProduit);
      });

      it('should not add a CatalogProduit to an array that contains it', () => {
        const catalogProduit: ICatalogProduit = { id: 123 };
        const catalogProduitCollection: ICatalogProduit[] = [
          {
            ...catalogProduit,
          },
          { id: 456 },
        ];
        expectedResult = service.addCatalogProduitToCollectionIfMissing(catalogProduitCollection, catalogProduit);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a CatalogProduit to an array that doesn't contain it", () => {
        const catalogProduit: ICatalogProduit = { id: 123 };
        const catalogProduitCollection: ICatalogProduit[] = [{ id: 456 }];
        expectedResult = service.addCatalogProduitToCollectionIfMissing(catalogProduitCollection, catalogProduit);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(catalogProduit);
      });

      it('should add only unique CatalogProduit to an array', () => {
        const catalogProduitArray: ICatalogProduit[] = [{ id: 123 }, { id: 456 }, { id: 91351 }];
        const catalogProduitCollection: ICatalogProduit[] = [{ id: 123 }];
        expectedResult = service.addCatalogProduitToCollectionIfMissing(catalogProduitCollection, ...catalogProduitArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const catalogProduit: ICatalogProduit = { id: 123 };
        const catalogProduit2: ICatalogProduit = { id: 456 };
        expectedResult = service.addCatalogProduitToCollectionIfMissing([], catalogProduit, catalogProduit2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(catalogProduit);
        expect(expectedResult).toContain(catalogProduit2);
      });

      it('should accept null and undefined values', () => {
        const catalogProduit: ICatalogProduit = { id: 123 };
        expectedResult = service.addCatalogProduitToCollectionIfMissing([], null, catalogProduit, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(catalogProduit);
      });

      it('should return initial array if no CatalogProduit is added', () => {
        const catalogProduitCollection: ICatalogProduit[] = [{ id: 123 }];
        expectedResult = service.addCatalogProduitToCollectionIfMissing(catalogProduitCollection, undefined, null);
        expect(expectedResult).toEqual(catalogProduitCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
