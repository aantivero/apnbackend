/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { PaynowTestModule } from '../../../test.module';
import { SaldoAppComponent } from '../../../../../../main/webapp/app/entities/saldo-app/saldo-app.component';
import { SaldoAppService } from '../../../../../../main/webapp/app/entities/saldo-app/saldo-app.service';
import { SaldoApp } from '../../../../../../main/webapp/app/entities/saldo-app/saldo-app.model';

describe('Component Tests', () => {

    describe('SaldoApp Management Component', () => {
        let comp: SaldoAppComponent;
        let fixture: ComponentFixture<SaldoAppComponent>;
        let service: SaldoAppService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [PaynowTestModule],
                declarations: [SaldoAppComponent],
                providers: [
                    SaldoAppService
                ]
            })
            .overrideTemplate(SaldoAppComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(SaldoAppComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(SaldoAppService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new HttpHeaders().append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of(new HttpResponse({
                    body: [new SaldoApp(123)],
                    headers
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.saldoApps[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
