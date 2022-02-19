import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ILotFacture, LotFacture } from '../lot-facture.model';

import { LotFactureService } from './lot-facture.service';

describe('LotFacture Service', () => {
  let service: LotFactureService;
  let httpMock: HttpTestingController;
  let elemDefault: ILotFacture;
  let expectedResult: ILotFacture | ILotFacture[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(LotFactureService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      quantite: 0,
      date: currentDate,
      montantTotal: 0,
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

    it('should create a LotFacture', () => {
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

      service.create(new LotFacture()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a LotFacture', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          quantite: 1,
          date: currentDate.format(DATE_TIME_FORMAT),
          montantTotal: 1,
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

    it('should partial update a LotFacture', () => {
      const patchObject = Object.assign(
        {
          quantite: 1,
          montantTotal: 1,
        },
        new LotFacture()
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

    it('should return a list of LotFacture', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          quantite: 1,
          date: currentDate.format(DATE_TIME_FORMAT),
          montantTotal: 1,
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

    it('should delete a LotFacture', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addLotFactureToCollectionIfMissing', () => {
      it('should add a LotFacture to an empty array', () => {
        const lotFacture: ILotFacture = { id: 123 };
        expectedResult = service.addLotFactureToCollectionIfMissing([], lotFacture);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(lotFacture);
      });

      it('should not add a LotFacture to an array that contains it', () => {
        const lotFacture: ILotFacture = { id: 123 };
        const lotFactureCollection: ILotFacture[] = [
          {
            ...lotFacture,
          },
          { id: 456 },
        ];
        expectedResult = service.addLotFactureToCollectionIfMissing(lotFactureCollection, lotFacture);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a LotFacture to an array that doesn't contain it", () => {
        const lotFacture: ILotFacture = { id: 123 };
        const lotFactureCollection: ILotFacture[] = [{ id: 456 }];
        expectedResult = service.addLotFactureToCollectionIfMissing(lotFactureCollection, lotFacture);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(lotFacture);
      });

      it('should add only unique LotFacture to an array', () => {
        const lotFactureArray: ILotFacture[] = [{ id: 123 }, { id: 456 }, { id: 2358 }];
        const lotFactureCollection: ILotFacture[] = [{ id: 123 }];
        expectedResult = service.addLotFactureToCollectionIfMissing(lotFactureCollection, ...lotFactureArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const lotFacture: ILotFacture = { id: 123 };
        const lotFacture2: ILotFacture = { id: 456 };
        expectedResult = service.addLotFactureToCollectionIfMissing([], lotFacture, lotFacture2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(lotFacture);
        expect(expectedResult).toContain(lotFacture2);
      });

      it('should accept null and undefined values', () => {
        const lotFacture: ILotFacture = { id: 123 };
        expectedResult = service.addLotFactureToCollectionIfMissing([], null, lotFacture, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(lotFacture);
      });

      it('should return initial array if no LotFacture is added', () => {
        const lotFactureCollection: ILotFacture[] = [{ id: 123 }];
        expectedResult = service.addLotFactureToCollectionIfMissing(lotFactureCollection, undefined, null);
        expect(expectedResult).toEqual(lotFactureCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
