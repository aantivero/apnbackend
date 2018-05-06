/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { PaynowTestModule } from '../../../test.module';
import { UserExtraDetailComponent } from '../../../../../../main/webapp/app/entities/user-extra/user-extra-detail.component';
import { UserExtraService } from '../../../../../../main/webapp/app/entities/user-extra/user-extra.service';
import { UserExtra } from '../../../../../../main/webapp/app/entities/user-extra/user-extra.model';

describe('Component Tests', () => {

    describe('UserExtra Management Detail Component', () => {
        let comp: UserExtraDetailComponent;
        let fixture: ComponentFixture<UserExtraDetailComponent>;
        let service: UserExtraService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [PaynowTestModule],
                declarations: [UserExtraDetailComponent],
                providers: [
                    UserExtraService
                ]
            })
            .overrideTemplate(UserExtraDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(UserExtraDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(UserExtraService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new HttpResponse({
                    body: new UserExtra(123)
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.userExtra).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
