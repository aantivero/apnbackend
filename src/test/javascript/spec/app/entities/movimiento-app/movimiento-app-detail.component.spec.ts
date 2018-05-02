/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { PaynowTestModule } from '../../../test.module';
import { MovimientoAppDetailComponent } from '../../../../../../main/webapp/app/entities/movimiento-app/movimiento-app-detail.component';
import { MovimientoAppService } from '../../../../../../main/webapp/app/entities/movimiento-app/movimiento-app.service';
import { MovimientoApp } from '../../../../../../main/webapp/app/entities/movimiento-app/movimiento-app.model';

describe('Component Tests', () => {

    describe('MovimientoApp Management Detail Component', () => {
        let comp: MovimientoAppDetailComponent;
        let fixture: ComponentFixture<MovimientoAppDetailComponent>;
        let service: MovimientoAppService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [PaynowTestModule],
                declarations: [MovimientoAppDetailComponent],
                providers: [
                    MovimientoAppService
                ]
            })
            .overrideTemplate(MovimientoAppDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(MovimientoAppDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(MovimientoAppService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new HttpResponse({
                    body: new MovimientoApp(123)
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.movimientoApp).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
