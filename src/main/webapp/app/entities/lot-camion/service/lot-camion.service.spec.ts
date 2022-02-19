import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ILotCamion, LotCamion } from '../lot-camion.model';

import { LotCamionService } from './lot-camion.service';

describe('LotCamion Service', () => {
  let service: LotCamionService;
  let httpMock: HttpTestingController;
  let elemDefault: ILotCamion;
  let expectedResult: ILotCamion | ILotCamion[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(LotCamionService);
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

    it('should create a LotCamion', () => {
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

      service.create(new LotCamion()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a LotCamion', () => {
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

    it('should partial update a LotCamion', () => {
      const patchObject = Object.assign(
        {
          date: currentDate.format(DATE_TIME_FORMAT),
        },
        new LotCamion()
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

    it('should return a list of LotCamion', () => {
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

    it('should delete a LotCamion', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addLotCamionToCollectionIfMissing', () => {
      it('should add a LotCamion to an empty array', () => {
        const lotCamion: ILotCamion = { id: 123 };
        expectedResult = service.addLotCamionToCollectionIfMissing([], lotCamion);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(lotCamion);
      });

      it('should not add a LotCamion to an array that contains it', () => {
        const lotCamion: ILotCamion = { id: 123 };
        const lotCamionCollection: ILotCamion[] = [
          {
            ...lotCamion,
          },
          { id: 456 },
        ];
        expectedResult = service.addLotCamionToCollectionIfMissing(lotCamionCollection, lotCamion);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a LotCamion to an array that doesn't contain it", () => {
        const lotCamion: ILotCamion = { id: 123 };
        const lotCamionCollection: ILotCamion[] = [{ id: 456 }];
        expectedResult = service.addLotCamionToCollectionIfMissing(lotCamionCollection, lotCamion);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(lotCamion);
      });

      it('should add only unique LotCamion to an array', () => {
        const lotCamionArray: ILotCamion[] = [{ id: 123 }, { id: 456 }, { id: 81292 }];
        const lotCamionCollection: ILotCamion[] = [{ id: 123 }];
        expectedResult = service.addLotCamionToCollectionIfMissing(lotCamionCollection, ...lotCamionArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const lotCamion: ILotCamion = { id: 123 };
        const lotCamion2: ILotCamion = { id: 456 };
        expectedResult = service.addLotCamionToCollectionIfMissing([], lotCamion, lotCamion2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(lotCamion);
        expect(expectedResult).toContain(lotCamion2);
      });

      it('should accept null and undefined values', () => {
        const lotCamion: ILotCamion = { id: 123 };
        expectedResult = service.addLotCamionToCollectionIfMissing([], null, lotCamion, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(lotCamion);
      });

      it('should return initial array if no LotCamion is added', () => {
        const lotCamionCollection: ILotCamion[] = [{ id: 123 }];
        expectedResult = service.addLotCamionToCollectionIfMissing(lotCamionCollection, undefined, null);
        expect(expectedResult).toEqual(lotCamionCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
