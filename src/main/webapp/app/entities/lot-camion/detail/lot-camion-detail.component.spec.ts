import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { LotCamionDetailComponent } from './lot-camion-detail.component';

describe('LotCamion Management Detail Component', () => {
  let comp: LotCamionDetailComponent;
  let fixture: ComponentFixture<LotCamionDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [LotCamionDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ lotCamion: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(LotCamionDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(LotCamionDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load lotCamion on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.lotCamion).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
