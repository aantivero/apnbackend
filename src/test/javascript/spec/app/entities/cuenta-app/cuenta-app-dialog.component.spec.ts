/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable } from 'rxjs/Observable';
import { JhiEventManager } from 'ng-jhipster';

import { PaynowTestModule } from '../../../test.module';
import { CuentaAppDialogComponent } from '../../../../../../main/webapp/app/entities/cuenta-app/cuenta-app-dialog.component';
import { CuentaAppService } from '../../../../../../main/webapp/app/entities/cuenta-app/cuenta-app.service';
import { CuentaApp } from '../../../../../../main/webapp/app/entities/cuenta-app/cuenta-app.model';
import { BancoService } from '../../../../../../main/webapp/app/entities/banco';
import { AppService } from '../../../../../../main/webapp/app/entities/app';

describe('Component Tests', () => {

    describe('CuentaApp Management Dialog Component', () => {
        let comp: CuentaAppDialogComponent;
        let fixture: ComponentFixture<CuentaAppDialogComponent>;
        let service: CuentaAppService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [PaynowTestModule],
                declarations: [CuentaAppDialogComponent],
                providers: [
                    BancoService,
                    AppService,
                    CuentaAppService
                ]
            })
            .overrideTemplate(CuentaAppDialogComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(CuentaAppDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(CuentaAppService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        const entity = new CuentaApp(123);
                        spyOn(service, 'update').and.returnValue(Observable.of(new HttpResponse({body: entity})));
                        comp.cuentaApp = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.update).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'cuentaAppListModification', content: 'OK'});
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    })
                )
            );

            it('Should call create service on save for new entity',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        const entity = new CuentaApp();
                        spyOn(service, 'create').and.returnValue(Observable.of(new HttpResponse({body: entity})));
                        comp.cuentaApp = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.create).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'cuentaAppListModification', content: 'OK'});
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    })
                )
            );
        });
    });

});
