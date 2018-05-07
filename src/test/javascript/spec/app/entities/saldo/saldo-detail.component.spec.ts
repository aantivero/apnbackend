/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { PaynowTestModule } from '../../../test.module';
import { SaldoDetailComponent } from '../../../../../../main/webapp/app/entities/saldo/saldo-detail.component';
import { SaldoService } from '../../../../../../main/webapp/app/entities/saldo/saldo.service';
import { Saldo } from '../../../../../../main/webapp/app/entities/saldo/saldo.model';

describe('Component Tests', () => {

    describe('Saldo Management Detail Component', () => {
        let comp: SaldoDetailComponent;
        let fixture: ComponentFixture<SaldoDetailComponent>;
        let service: SaldoService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [PaynowTestModule],
                declarations: [SaldoDetailComponent],
                providers: [
                    SaldoService
                ]
            })
            .overrideTemplate(SaldoDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(SaldoDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(SaldoService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new HttpResponse({
                    body: new Saldo(123)
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.saldo).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
