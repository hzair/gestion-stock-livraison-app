import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { IFactureVente, FactureVente } from '../facture-vente.model';
import { FactureVenteService } from '../service/facture-vente.service';

import { FactureVenteRoutingResolveService } from './facture-vente-routing-resolve.service';

describe('FactureVente routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: FactureVenteRoutingResolveService;
  let service: FactureVenteService;
  let resultFactureVente: IFactureVente | undefined;

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
    routingResolveService = TestBed.inject(FactureVenteRoutingResolveService);
    service = TestBed.inject(FactureVenteService);
    resultFactureVente = undefined;
  });

  describe('resolve', () => {
    it('should return IFactureVente returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultFactureVente = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultFactureVente).toEqual({ id: 123 });
    });

    it('should return new IFactureVente if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultFactureVente = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultFactureVente).toEqual(new FactureVente());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as FactureVente })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultFactureVente = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultFactureVente).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
