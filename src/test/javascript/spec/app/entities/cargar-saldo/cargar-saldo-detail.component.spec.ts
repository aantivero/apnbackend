/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { PaynowTestModule } from '../../../test.module';
import { CargarSaldoDetailComponent } from '../../../../../../main/webapp/app/entities/cargar-saldo/cargar-saldo-detail.component';
import { CargarSaldoService } from '../../../../../../main/webapp/app/entities/cargar-saldo/cargar-saldo.service';
import { CargarSaldo } from '../../../../../../main/webapp/app/entities/cargar-saldo/cargar-saldo.model';

describe('Component Tests', () => {

    describe('CargarSaldo Management Detail Component', () => {
        let comp: CargarSaldoDetailComponent;
        let fixture: ComponentFixture<CargarSaldoDetailComponent>;
        let service: CargarSaldoService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [PaynowTestModule],
                declarations: [CargarSaldoDetailComponent],
                providers: [
                    CargarSaldoService
                ]
            })
            .overrideTemplate(CargarSaldoDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(CargarSaldoDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(CargarSaldoService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new HttpResponse({
                    body: new CargarSaldo(123)
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.cargarSaldo).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
