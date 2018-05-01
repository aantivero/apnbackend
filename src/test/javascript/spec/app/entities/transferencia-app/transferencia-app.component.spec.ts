/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { PaynowTestModule } from '../../../test.module';
import { TransferenciaAppComponent } from '../../../../../../main/webapp/app/entities/transferencia-app/transferencia-app.component';
import { TransferenciaAppService } from '../../../../../../main/webapp/app/entities/transferencia-app/transferencia-app.service';
import { TransferenciaApp } from '../../../../../../main/webapp/app/entities/transferencia-app/transferencia-app.model';

describe('Component Tests', () => {

    describe('TransferenciaApp Management Component', () => {
        let comp: TransferenciaAppComponent;
        let fixture: ComponentFixture<TransferenciaAppComponent>;
        let service: TransferenciaAppService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [PaynowTestModule],
                declarations: [TransferenciaAppComponent],
                providers: [
                    TransferenciaAppService
                ]
            })
            .overrideTemplate(TransferenciaAppComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(TransferenciaAppComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(TransferenciaAppService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new HttpHeaders().append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of(new HttpResponse({
                    body: [new TransferenciaApp(123)],
                    headers
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.transferenciaApps[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
