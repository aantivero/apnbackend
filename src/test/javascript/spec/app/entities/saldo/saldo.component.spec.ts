/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { PaynowTestModule } from '../../../test.module';
import { SaldoComponent } from '../../../../../../main/webapp/app/entities/saldo/saldo.component';
import { SaldoService } from '../../../../../../main/webapp/app/entities/saldo/saldo.service';
import { Saldo } from '../../../../../../main/webapp/app/entities/saldo/saldo.model';

describe('Component Tests', () => {

    describe('Saldo Management Component', () => {
        let comp: SaldoComponent;
        let fixture: ComponentFixture<SaldoComponent>;
        let service: SaldoService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [PaynowTestModule],
                declarations: [SaldoComponent],
                providers: [
                    SaldoService
                ]
            })
            .overrideTemplate(SaldoComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(SaldoComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(SaldoService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new HttpHeaders().append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of(new HttpResponse({
                    body: [new Saldo(123)],
                    headers
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.saldos[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
