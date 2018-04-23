/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable } from 'rxjs/Observable';
import { JhiEventManager } from 'ng-jhipster';

import { PaynowTestModule } from '../../../test.module';
import { CuentaAppDeleteDialogComponent } from '../../../../../../main/webapp/app/entities/cuenta-app/cuenta-app-delete-dialog.component';
import { CuentaAppService } from '../../../../../../main/webapp/app/entities/cuenta-app/cuenta-app.service';

describe('Component Tests', () => {

    describe('CuentaApp Management Delete Component', () => {
        let comp: CuentaAppDeleteDialogComponent;
        let fixture: ComponentFixture<CuentaAppDeleteDialogComponent>;
        let service: CuentaAppService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [PaynowTestModule],
                declarations: [CuentaAppDeleteDialogComponent],
                providers: [
                    CuentaAppService
                ]
            })
            .overrideTemplate(CuentaAppDeleteDialogComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(CuentaAppDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(CuentaAppService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('confirmDelete', () => {
            it('Should call delete service on confirmDelete',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        spyOn(service, 'delete').and.returnValue(Observable.of({}));

                        // WHEN
                        comp.confirmDelete(123);
                        tick();

                        // THEN
                        expect(service.delete).toHaveBeenCalledWith(123);
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
                    })
                )
            );
        });
    });

});
