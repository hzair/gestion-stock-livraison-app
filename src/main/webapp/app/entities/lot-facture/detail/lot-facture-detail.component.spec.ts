import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { LotFactureDetailComponent } from './lot-facture-detail.component';

describe('LotFacture Management Detail Component', () => {
  let comp: LotFactureDetailComponent;
  let fixture: ComponentFixture<LotFactureDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [LotFactureDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ lotFacture: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(LotFactureDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(LotFactureDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load lotFacture on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.lotFacture).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
