/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { PaynowTestModule } from '../../../test.module';
import { MovimientoAppComponent } from '../../../../../../main/webapp/app/entities/movimiento-app/movimiento-app.component';
import { MovimientoAppService } from '../../../../../../main/webapp/app/entities/movimiento-app/movimiento-app.service';
import { MovimientoApp } from '../../../../../../main/webapp/app/entities/movimiento-app/movimiento-app.model';

describe('Component Tests', () => {

    describe('MovimientoApp Management Component', () => {
        let comp: MovimientoAppComponent;
        let fixture: ComponentFixture<MovimientoAppComponent>;
        let service: MovimientoAppService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [PaynowTestModule],
                declarations: [MovimientoAppComponent],
                providers: [
                    MovimientoAppService
                ]
            })
            .overrideTemplate(MovimientoAppComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(MovimientoAppComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(MovimientoAppService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new HttpHeaders().append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of(new HttpResponse({
                    body: [new MovimientoApp(123)],
                    headers
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.movimientoApps[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
