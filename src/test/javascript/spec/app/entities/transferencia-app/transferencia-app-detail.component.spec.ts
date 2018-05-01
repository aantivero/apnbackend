/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { PaynowTestModule } from '../../../test.module';
import { TransferenciaAppDetailComponent } from '../../../../../../main/webapp/app/entities/transferencia-app/transferencia-app-detail.component';
import { TransferenciaAppService } from '../../../../../../main/webapp/app/entities/transferencia-app/transferencia-app.service';
import { TransferenciaApp } from '../../../../../../main/webapp/app/entities/transferencia-app/transferencia-app.model';

describe('Component Tests', () => {

    describe('TransferenciaApp Management Detail Component', () => {
        let comp: TransferenciaAppDetailComponent;
        let fixture: ComponentFixture<TransferenciaAppDetailComponent>;
        let service: TransferenciaAppService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [PaynowTestModule],
                declarations: [TransferenciaAppDetailComponent],
                providers: [
                    TransferenciaAppService
                ]
            })
            .overrideTemplate(TransferenciaAppDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(TransferenciaAppDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(TransferenciaAppService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new HttpResponse({
                    body: new TransferenciaApp(123)
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.transferenciaApp).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
