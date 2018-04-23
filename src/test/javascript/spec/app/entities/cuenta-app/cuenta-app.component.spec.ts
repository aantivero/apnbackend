/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { PaynowTestModule } from '../../../test.module';
import { CuentaAppComponent } from '../../../../../../main/webapp/app/entities/cuenta-app/cuenta-app.component';
import { CuentaAppService } from '../../../../../../main/webapp/app/entities/cuenta-app/cuenta-app.service';
import { CuentaApp } from '../../../../../../main/webapp/app/entities/cuenta-app/cuenta-app.model';

describe('Component Tests', () => {

    describe('CuentaApp Management Component', () => {
        let comp: CuentaAppComponent;
        let fixture: ComponentFixture<CuentaAppComponent>;
        let service: CuentaAppService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [PaynowTestModule],
                declarations: [CuentaAppComponent],
                providers: [
                    CuentaAppService
                ]
            })
            .overrideTemplate(CuentaAppComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(CuentaAppComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(CuentaAppService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new HttpHeaders().append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of(new HttpResponse({
                    body: [new CuentaApp(123)],
                    headers
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.cuentaApps[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
