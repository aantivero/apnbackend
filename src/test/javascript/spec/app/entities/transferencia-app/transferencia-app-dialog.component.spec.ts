/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable } from 'rxjs/Observable';
import { JhiEventManager } from 'ng-jhipster';

import { PaynowTestModule } from '../../../test.module';
import { TransferenciaAppDialogComponent } from '../../../../../../main/webapp/app/entities/transferencia-app/transferencia-app-dialog.component';
import { TransferenciaAppService } from '../../../../../../main/webapp/app/entities/transferencia-app/transferencia-app.service';
import { TransferenciaApp } from '../../../../../../main/webapp/app/entities/transferencia-app/transferencia-app.model';
import { CuentaAppService } from '../../../../../../main/webapp/app/entities/cuenta-app';
import { BancoService } from '../../../../../../main/webapp/app/entities/banco';

describe('Component Tests', () => {

    describe('TransferenciaApp Management Dialog Component', () => {
        let comp: TransferenciaAppDialogComponent;
        let fixture: ComponentFixture<TransferenciaAppDialogComponent>;
        let service: TransferenciaAppService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [PaynowTestModule],
                declarations: [TransferenciaAppDialogComponent],
                providers: [
                    CuentaAppService,
                    BancoService,
                    TransferenciaAppService
                ]
            })
            .overrideTemplate(TransferenciaAppDialogComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(TransferenciaAppDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(TransferenciaAppService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        const entity = new TransferenciaApp(123);
                        spyOn(service, 'update').and.returnValue(Observable.of(new HttpResponse({body: entity})));
                        comp.transferenciaApp = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.update).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'transferenciaAppListModification', content: 'OK'});
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    })
                )
            );

            it('Should call create service on save for new entity',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        const entity = new TransferenciaApp();
                        spyOn(service, 'create').and.returnValue(Observable.of(new HttpResponse({body: entity})));
                        comp.transferenciaApp = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.create).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'transferenciaAppListModification', content: 'OK'});
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    })
                )
            );
        });
    });

});
