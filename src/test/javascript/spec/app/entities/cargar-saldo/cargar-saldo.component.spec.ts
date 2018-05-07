/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { PaynowTestModule } from '../../../test.module';
import { CargarSaldoComponent } from '../../../../../../main/webapp/app/entities/cargar-saldo/cargar-saldo.component';
import { CargarSaldoService } from '../../../../../../main/webapp/app/entities/cargar-saldo/cargar-saldo.service';
import { CargarSaldo } from '../../../../../../main/webapp/app/entities/cargar-saldo/cargar-saldo.model';

describe('Component Tests', () => {

    describe('CargarSaldo Management Component', () => {
        let comp: CargarSaldoComponent;
        let fixture: ComponentFixture<CargarSaldoComponent>;
        let service: CargarSaldoService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [PaynowTestModule],
                declarations: [CargarSaldoComponent],
                providers: [
                    CargarSaldoService
                ]
            })
            .overrideTemplate(CargarSaldoComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(CargarSaldoComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(CargarSaldoService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new HttpHeaders().append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of(new HttpResponse({
                    body: [new CargarSaldo(123)],
                    headers
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.cargarSaldos[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
