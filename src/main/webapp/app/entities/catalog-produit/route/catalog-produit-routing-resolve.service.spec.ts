import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { ICatalogProduit, CatalogProduit } from '../catalog-produit.model';
import { CatalogProduitService } from '../service/catalog-produit.service';

import { CatalogProduitRoutingResolveService } from './catalog-produit-routing-resolve.service';

describe('CatalogProduit routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: CatalogProduitRoutingResolveService;
  let service: CatalogProduitService;
  let resultCatalogProduit: ICatalogProduit | undefined;

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
    routingResolveService = TestBed.inject(CatalogProduitRoutingResolveService);
    service = TestBed.inject(CatalogProduitService);
    resultCatalogProduit = undefined;
  });

  describe('resolve', () => {
    it('should return ICatalogProduit returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCatalogProduit = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultCatalogProduit).toEqual({ id: 123 });
    });

    it('should return new ICatalogProduit if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCatalogProduit = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultCatalogProduit).toEqual(new CatalogProduit());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as CatalogProduit })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCatalogProduit = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultCatalogProduit).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
