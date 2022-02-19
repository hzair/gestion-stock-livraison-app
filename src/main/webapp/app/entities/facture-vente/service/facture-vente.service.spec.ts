import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IFactureVente, FactureVente } from '../facture-vente.model';

import { FactureVenteService } from './facture-vente.service';

describe('FactureVente Service', () => {
  let service: FactureVenteService;
  let httpMock: HttpTestingController;
  let elemDefault: IFactureVente;
  let expectedResult: IFactureVente | IFactureVente[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(FactureVenteService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      photoContentType: 'image/png',
      photo: 'AAAAAAA',
      idFonc: 'AAAAAAA',
      designation: 'AAAAAAA',
      description: 'AAAAAAA',
      montant: 0,
      date: currentDate,
      payee: false,
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

    it('should create a FactureVente', () => {
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

      service.create(new FactureVente()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a FactureVente', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          photo: 'BBBBBB',
          idFonc: 'BBBBBB',
          designation: 'BBBBBB',
          description: 'BBBBBB',
          montant: 1,
          date: currentDate.format(DATE_TIME_FORMAT),
          payee: true,
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

    it('should partial update a FactureVente', () => {
      const patchObject = Object.assign(
        {
          photo: 'BBBBBB',
          idFonc: 'BBBBBB',
          description: 'BBBBBB',
          date: currentDate.format(DATE_TIME_FORMAT),
          payee: true,
        },
        new FactureVente()
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

    it('should return a list of FactureVente', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          photo: 'BBBBBB',
          idFonc: 'BBBBBB',
          designation: 'BBBBBB',
          description: 'BBBBBB',
          montant: 1,
          date: currentDate.format(DATE_TIME_FORMAT),
          payee: true,
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

    it('should delete a FactureVente', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addFactureVenteToCollectionIfMissing', () => {
      it('should add a FactureVente to an empty array', () => {
        const factureVente: IFactureVente = { id: 123 };
        expectedResult = service.addFactureVenteToCollectionIfMissing([], factureVente);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(factureVente);
      });

      it('should not add a FactureVente to an array that contains it', () => {
        const factureVente: IFactureVente = { id: 123 };
        const factureVenteCollection: IFactureVente[] = [
          {
            ...factureVente,
          },
          { id: 456 },
        ];
        expectedResult = service.addFactureVenteToCollectionIfMissing(factureVenteCollection, factureVente);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a FactureVente to an array that doesn't contain it", () => {
        const factureVente: IFactureVente = { id: 123 };
        const factureVenteCollection: IFactureVente[] = [{ id: 456 }];
        expectedResult = service.addFactureVenteToCollectionIfMissing(factureVenteCollection, factureVente);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(factureVente);
      });

      it('should add only unique FactureVente to an array', () => {
        const factureVenteArray: IFactureVente[] = [{ id: 123 }, { id: 456 }, { id: 5443 }];
        const factureVenteCollection: IFactureVente[] = [{ id: 123 }];
        expectedResult = service.addFactureVenteToCollectionIfMissing(factureVenteCollection, ...factureVenteArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const factureVente: IFactureVente = { id: 123 };
        const factureVente2: IFactureVente = { id: 456 };
        expectedResult = service.addFactureVenteToCollectionIfMissing([], factureVente, factureVente2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(factureVente);
        expect(expectedResult).toContain(factureVente2);
      });

      it('should accept null and undefined values', () => {
        const factureVente: IFactureVente = { id: 123 };
        expectedResult = service.addFactureVenteToCollectionIfMissing([], null, factureVente, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(factureVente);
      });

      it('should return initial array if no FactureVente is added', () => {
        const factureVenteCollection: IFactureVente[] = [{ id: 123 }];
        expectedResult = service.addFactureVenteToCollectionIfMissing(factureVenteCollection, undefined, null);
        expect(expectedResult).toEqual(factureVenteCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
