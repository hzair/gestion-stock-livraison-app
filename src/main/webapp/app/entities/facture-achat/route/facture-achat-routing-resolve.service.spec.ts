import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { IFactureAchat, FactureAchat } from '../facture-achat.model';
import { FactureAchatService } from '../service/facture-achat.service';

import { FactureAchatRoutingResolveService } from './facture-achat-routing-resolve.service';

describe('FactureAchat routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: FactureAchatRoutingResolveService;
  let service: FactureAchatService;
  let resultFactureAchat: IFactureAchat | undefined;

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
    routingResolveService = TestBed.inject(FactureAchatRoutingResolveService);
    service = TestBed.inject(FactureAchatService);
    resultFactureAchat = undefined;
  });

  describe('resolve', () => {
    it('should return IFactureAchat returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultFactureAchat = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultFactureAchat).toEqual({ id: 123 });
    });

    it('should return new IFactureAchat if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultFactureAchat = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultFactureAchat).toEqual(new FactureAchat());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as FactureAchat })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultFactureAchat = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultFactureAchat).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
