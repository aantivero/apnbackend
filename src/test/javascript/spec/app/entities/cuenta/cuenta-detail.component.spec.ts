/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { PaynowTestModule } from '../../../test.module';
import { CuentaDetailComponent } from '../../../../../../main/webapp/app/entities/cuenta/cuenta-detail.component';
import { CuentaService } from '../../../../../../main/webapp/app/entities/cuenta/cuenta.service';
import { Cuenta } from '../../../../../../main/webapp/app/entities/cuenta/cuenta.model';

describe('Component Tests', () => {

    describe('Cuenta Management Detail Component', () => {
        let comp: CuentaDetailComponent;
        let fixture: ComponentFixture<CuentaDetailComponent>;
        let service: CuentaService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [PaynowTestModule],
                declarations: [CuentaDetailComponent],
                providers: [
                    CuentaService
                ]
            })
            .overrideTemplate(CuentaDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(CuentaDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(CuentaService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new HttpResponse({
                    body: new Cuenta(123)
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.cuenta).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
