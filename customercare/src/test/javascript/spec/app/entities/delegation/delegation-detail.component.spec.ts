import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';
import { JhiDataUtils } from 'ng-jhipster';

import { CustomercareTestModule } from '../../../test.module';
import { DelegationDetailComponent } from 'app/entities/delegation/delegation-detail.component';
import { Delegation } from 'app/shared/model/delegation.model';

describe('Component Tests', () => {
  describe('Delegation Management Detail Component', () => {
    let comp: DelegationDetailComponent;
    let fixture: ComponentFixture<DelegationDetailComponent>;
    let dataUtils: JhiDataUtils;
    const route = ({ data: of({ delegation: new Delegation(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [CustomercareTestModule],
        declarations: [DelegationDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(DelegationDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(DelegationDetailComponent);
      comp = fixture.componentInstance;
      dataUtils = fixture.debugElement.injector.get(JhiDataUtils);
    });

    describe('OnInit', () => {
      it('Should load delegation on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.delegation).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });

    describe('byteSize', () => {
      it('Should call byteSize from JhiDataUtils', () => {
        // GIVEN
        spyOn(dataUtils, 'byteSize');
        const fakeBase64 = 'fake base64';

        // WHEN
        comp.byteSize(fakeBase64);

        // THEN
        expect(dataUtils.byteSize).toBeCalledWith(fakeBase64);
      });
    });

    describe('openFile', () => {
      it('Should call openFile from JhiDataUtils', () => {
        // GIVEN
        spyOn(dataUtils, 'openFile');
        const fakeContentType = 'fake content type';
        const fakeBase64 = 'fake base64';

        // WHEN
        comp.openFile(fakeContentType, fakeBase64);

        // THEN
        expect(dataUtils.openFile).toBeCalledWith(fakeContentType, fakeBase64);
      });
    });
  });
});
