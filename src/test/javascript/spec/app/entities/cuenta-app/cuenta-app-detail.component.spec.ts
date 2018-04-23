/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { PaynowTestModule } from '../../../test.module';
import { CuentaAppDetailComponent } from '../../../../../../main/webapp/app/entities/cuenta-app/cuenta-app-detail.component';
import { CuentaAppService } from '../../../../../../main/webapp/app/entities/cuenta-app/cuenta-app.service';
import { CuentaApp } from '../../../../../../main/webapp/app/entities/cuenta-app/cuenta-app.model';

describe('Component Tests', () => {

    describe('CuentaApp Management Detail Component', () => {
        let comp: CuentaAppDetailComponent;
        let fixture: ComponentFixture<CuentaAppDetailComponent>;
        let service: CuentaAppService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [PaynowTestModule],
                declarations: [CuentaAppDetailComponent],
                providers: [
                    CuentaAppService
                ]
            })
            .overrideTemplate(CuentaAppDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(CuentaAppDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(CuentaAppService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new HttpResponse({
                    body: new CuentaApp(123)
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.cuentaApp).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
