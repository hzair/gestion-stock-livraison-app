import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { ICatalogTypeProduit, CatalogTypeProduit } from '../catalog-type-produit.model';
import { CatalogTypeProduitService } from '../service/catalog-type-produit.service';

import { CatalogTypeProduitRoutingResolveService } from './catalog-type-produit-routing-resolve.service';

describe('CatalogTypeProduit routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: CatalogTypeProduitRoutingResolveService;
  let service: CatalogTypeProduitService;
  let resultCatalogTypeProduit: ICatalogTypeProduit | undefined;

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
    routingResolveService = TestBed.inject(CatalogTypeProduitRoutingResolveService);
    service = TestBed.inject(CatalogTypeProduitService);
    resultCatalogTypeProduit = undefined;
  });

  describe('resolve', () => {
    it('should return ICatalogTypeProduit returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCatalogTypeProduit = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultCatalogTypeProduit).toEqual({ id: 123 });
    });

    it('should return new ICatalogTypeProduit if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCatalogTypeProduit = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultCatalogTypeProduit).toEqual(new CatalogTypeProduit());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as CatalogTypeProduit })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCatalogTypeProduit = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultCatalogTypeProduit).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
