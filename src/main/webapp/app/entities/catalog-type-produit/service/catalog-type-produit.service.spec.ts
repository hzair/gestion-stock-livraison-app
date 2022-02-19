import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ICatalogTypeProduit, CatalogTypeProduit } from '../catalog-type-produit.model';

import { CatalogTypeProduitService } from './catalog-type-produit.service';

describe('CatalogTypeProduit Service', () => {
  let service: CatalogTypeProduitService;
  let httpMock: HttpTestingController;
  let elemDefault: ICatalogTypeProduit;
  let expectedResult: ICatalogTypeProduit | ICatalogTypeProduit[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(CatalogTypeProduitService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      imageContentType: 'image/png',
      image: 'AAAAAAA',
      idFonc: 'AAAAAAA',
      designation: 'AAAAAAA',
      description: 'AAAAAAA',
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign({}, elemDefault);

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a CatalogTypeProduit', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new CatalogTypeProduit()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a CatalogTypeProduit', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          image: 'BBBBBB',
          idFonc: 'BBBBBB',
          designation: 'BBBBBB',
          description: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a CatalogTypeProduit', () => {
      const patchObject = Object.assign(
        {
          idFonc: 'BBBBBB',
        },
        new CatalogTypeProduit()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of CatalogTypeProduit', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          image: 'BBBBBB',
          idFonc: 'BBBBBB',
          designation: 'BBBBBB',
          description: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a CatalogTypeProduit', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addCatalogTypeProduitToCollectionIfMissing', () => {
      it('should add a CatalogTypeProduit to an empty array', () => {
        const catalogTypeProduit: ICatalogTypeProduit = { id: 123 };
        expectedResult = service.addCatalogTypeProduitToCollectionIfMissing([], catalogTypeProduit);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(catalogTypeProduit);
      });

      it('should not add a CatalogTypeProduit to an array that contains it', () => {
        const catalogTypeProduit: ICatalogTypeProduit = { id: 123 };
        const catalogTypeProduitCollection: ICatalogTypeProduit[] = [
          {
            ...catalogTypeProduit,
          },
          { id: 456 },
        ];
        expectedResult = service.addCatalogTypeProduitToCollectionIfMissing(catalogTypeProduitCollection, catalogTypeProduit);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a CatalogTypeProduit to an array that doesn't contain it", () => {
        const catalogTypeProduit: ICatalogTypeProduit = { id: 123 };
        const catalogTypeProduitCollection: ICatalogTypeProduit[] = [{ id: 456 }];
        expectedResult = service.addCatalogTypeProduitToCollectionIfMissing(catalogTypeProduitCollection, catalogTypeProduit);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(catalogTypeProduit);
      });

      it('should add only unique CatalogTypeProduit to an array', () => {
        const catalogTypeProduitArray: ICatalogTypeProduit[] = [{ id: 123 }, { id: 456 }, { id: 83506 }];
        const catalogTypeProduitCollection: ICatalogTypeProduit[] = [{ id: 123 }];
        expectedResult = service.addCatalogTypeProduitToCollectionIfMissing(catalogTypeProduitCollection, ...catalogTypeProduitArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const catalogTypeProduit: ICatalogTypeProduit = { id: 123 };
        const catalogTypeProduit2: ICatalogTypeProduit = { id: 456 };
        expectedResult = service.addCatalogTypeProduitToCollectionIfMissing([], catalogTypeProduit, catalogTypeProduit2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(catalogTypeProduit);
        expect(expectedResult).toContain(catalogTypeProduit2);
      });

      it('should accept null and undefined values', () => {
        const catalogTypeProduit: ICatalogTypeProduit = { id: 123 };
        expectedResult = service.addCatalogTypeProduitToCollectionIfMissing([], null, catalogTypeProduit, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(catalogTypeProduit);
      });

      it('should return initial array if no CatalogTypeProduit is added', () => {
        const catalogTypeProduitCollection: ICatalogTypeProduit[] = [{ id: 123 }];
        expectedResult = service.addCatalogTypeProduitToCollectionIfMissing(catalogTypeProduitCollection, undefined, null);
        expect(expectedResult).toEqual(catalogTypeProduitCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
