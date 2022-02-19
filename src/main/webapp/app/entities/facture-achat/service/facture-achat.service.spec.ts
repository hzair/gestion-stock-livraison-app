import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IFactureAchat, FactureAchat } from '../facture-achat.model';

import { FactureAchatService } from './facture-achat.service';

describe('FactureAchat Service', () => {
  let service: FactureAchatService;
  let httpMock: HttpTestingController;
  let elemDefault: IFactureAchat;
  let expectedResult: IFactureAchat | IFactureAchat[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(FactureAchatService);
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

    it('should create a FactureAchat', () => {
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

      service.create(new FactureAchat()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a FactureAchat', () => {
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

    it('should partial update a FactureAchat', () => {
      const patchObject = Object.assign(
        {
          idFonc: 'BBBBBB',
          montant: 1,
          payee: true,
        },
        new FactureAchat()
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

    it('should return a list of FactureAchat', () => {
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

    it('should delete a FactureAchat', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addFactureAchatToCollectionIfMissing', () => {
      it('should add a FactureAchat to an empty array', () => {
        const factureAchat: IFactureAchat = { id: 123 };
        expectedResult = service.addFactureAchatToCollectionIfMissing([], factureAchat);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(factureAchat);
      });

      it('should not add a FactureAchat to an array that contains it', () => {
        const factureAchat: IFactureAchat = { id: 123 };
        const factureAchatCollection: IFactureAchat[] = [
          {
            ...factureAchat,
          },
          { id: 456 },
        ];
        expectedResult = service.addFactureAchatToCollectionIfMissing(factureAchatCollection, factureAchat);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a FactureAchat to an array that doesn't contain it", () => {
        const factureAchat: IFactureAchat = { id: 123 };
        const factureAchatCollection: IFactureAchat[] = [{ id: 456 }];
        expectedResult = service.addFactureAchatToCollectionIfMissing(factureAchatCollection, factureAchat);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(factureAchat);
      });

      it('should add only unique FactureAchat to an array', () => {
        const factureAchatArray: IFactureAchat[] = [{ id: 123 }, { id: 456 }, { id: 3327 }];
        const factureAchatCollection: IFactureAchat[] = [{ id: 123 }];
        expectedResult = service.addFactureAchatToCollectionIfMissing(factureAchatCollection, ...factureAchatArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const factureAchat: IFactureAchat = { id: 123 };
        const factureAchat2: IFactureAchat = { id: 456 };
        expectedResult = service.addFactureAchatToCollectionIfMissing([], factureAchat, factureAchat2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(factureAchat);
        expect(expectedResult).toContain(factureAchat2);
      });

      it('should accept null and undefined values', () => {
        const factureAchat: IFactureAchat = { id: 123 };
        expectedResult = service.addFactureAchatToCollectionIfMissing([], null, factureAchat, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(factureAchat);
      });

      it('should return initial array if no FactureAchat is added', () => {
        const factureAchatCollection: IFactureAchat[] = [{ id: 123 }];
        expectedResult = service.addFactureAchatToCollectionIfMissing(factureAchatCollection, undefined, null);
        expect(expectedResult).toEqual(factureAchatCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
