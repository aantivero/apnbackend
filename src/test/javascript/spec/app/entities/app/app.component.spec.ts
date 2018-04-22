/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { PaynowTestModule } from '../../../test.module';
import { AppComponent } from '../../../../../../main/webapp/app/entities/app/app.component';
import { AppService } from '../../../../../../main/webapp/app/entities/app/app.service';
import { App } from '../../../../../../main/webapp/app/entities/app/app.model';

describe('Component Tests', () => {

    describe('App Management Component', () => {
        let comp: AppComponent;
        let fixture: ComponentFixture<AppComponent>;
        let service: AppService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [PaynowTestModule],
                declarations: [AppComponent],
                providers: [
                    AppService
                ]
            })
            .overrideTemplate(AppComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(AppComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(AppService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new HttpHeaders().append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of(new HttpResponse({
                    body: [new App(123)],
                    headers
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.apps[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
