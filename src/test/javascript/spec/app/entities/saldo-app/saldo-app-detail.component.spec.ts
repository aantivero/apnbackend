/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { PaynowTestModule } from '../../../test.module';
import { SaldoAppDetailComponent } from '../../../../../../main/webapp/app/entities/saldo-app/saldo-app-detail.component';
import { SaldoAppService } from '../../../../../../main/webapp/app/entities/saldo-app/saldo-app.service';
import { SaldoApp } from '../../../../../../main/webapp/app/entities/saldo-app/saldo-app.model';

describe('Component Tests', () => {

    describe('SaldoApp Management Detail Component', () => {
        let comp: SaldoAppDetailComponent;
        let fixture: ComponentFixture<SaldoAppDetailComponent>;
        let service: SaldoAppService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [PaynowTestModule],
                declarations: [SaldoAppDetailComponent],
                providers: [
                    SaldoAppService
                ]
            })
            .overrideTemplate(SaldoAppDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(SaldoAppDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(SaldoAppService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new HttpResponse({
                    body: new SaldoApp(123)
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.saldoApp).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
