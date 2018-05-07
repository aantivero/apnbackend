/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { PaynowTestModule } from '../../../test.module';
import { CuentaComponent } from '../../../../../../main/webapp/app/entities/cuenta/cuenta.component';
import { CuentaService } from '../../../../../../main/webapp/app/entities/cuenta/cuenta.service';
import { Cuenta } from '../../../../../../main/webapp/app/entities/cuenta/cuenta.model';

describe('Component Tests', () => {

    describe('Cuenta Management Component', () => {
        let comp: CuentaComponent;
        let fixture: ComponentFixture<CuentaComponent>;
        let service: CuentaService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [PaynowTestModule],
                declarations: [CuentaComponent],
                providers: [
                    CuentaService
                ]
            })
            .overrideTemplate(CuentaComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(CuentaComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(CuentaService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new HttpHeaders().append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of(new HttpResponse({
                    body: [new Cuenta(123)],
                    headers
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.cuentas[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
