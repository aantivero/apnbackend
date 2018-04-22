/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { PaynowTestModule } from '../../../test.module';
import { AppDetailComponent } from '../../../../../../main/webapp/app/entities/app/app-detail.component';
import { AppService } from '../../../../../../main/webapp/app/entities/app/app.service';
import { App } from '../../../../../../main/webapp/app/entities/app/app.model';

describe('Component Tests', () => {

    describe('App Management Detail Component', () => {
        let comp: AppDetailComponent;
        let fixture: ComponentFixture<AppDetailComponent>;
        let service: AppService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [PaynowTestModule],
                declarations: [AppDetailComponent],
                providers: [
                    AppService
                ]
            })
            .overrideTemplate(AppDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(AppDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(AppService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new HttpResponse({
                    body: new App(123)
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.app).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
