import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { ILotFacture, LotFacture } from '../lot-facture.model';
import { LotFactureService } from '../service/lot-facture.service';

import { LotFactureRoutingResolveService } from './lot-facture-routing-resolve.service';

describe('LotFacture routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: LotFactureRoutingResolveService;
  let service: LotFactureService;
  let resultLotFacture: ILotFacture | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            snapshot: {
              paramMap: convertToParamMap({}),
            },
          },
        },
      ],
    });
    mockRouter = TestBed.inject(Router);
    jest.spyOn(mockRouter, 'navigate').mockImplementation(() => Promise.resolve(true));
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRoute).snapshot;
    routingResolveService = TestBed.inject(LotFactureRoutingResolveService);
    service = TestBed.inject(LotFactureService);
    resultLotFacture = undefined;
  });

  describe('resolve', () => {
    it('should return ILotFacture returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultLotFacture = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultLotFacture).toEqual({ id: 123 });
    });

    it('should return new ILotFacture if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultLotFacture = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultLotFacture).toEqual(new LotFacture());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as LotFacture })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultLotFacture = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultLotFacture).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
