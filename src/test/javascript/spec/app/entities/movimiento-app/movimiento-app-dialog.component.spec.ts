/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable } from 'rxjs/Observable';
import { JhiEventManager } from 'ng-jhipster';

import { PaynowTestModule } from '../../../test.module';
import { MovimientoAppDialogComponent } from '../../../../../../main/webapp/app/entities/movimiento-app/movimiento-app-dialog.component';
import { MovimientoAppService } from '../../../../../../main/webapp/app/entities/movimiento-app/movimiento-app.service';
import { MovimientoApp } from '../../../../../../main/webapp/app/entities/movimiento-app/movimiento-app.model';
import { BancoService } from '../../../../../../main/webapp/app/entities/banco';

describe('Component Tests', () => {

    describe('MovimientoApp Management Dialog Component', () => {
        let comp: MovimientoAppDialogComponent;
        let fixture: ComponentFixture<MovimientoAppDialogComponent>;
        let service: MovimientoAppService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [PaynowTestModule],
                declarations: [MovimientoAppDialogComponent],
                providers: [
                    BancoService,
                    MovimientoAppService
                ]
            })
            .overrideTemplate(MovimientoAppDialogComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(MovimientoAppDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(MovimientoAppService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        const entity = new MovimientoApp(123);
                        spyOn(service, 'update').and.returnValue(Observable.of(new HttpResponse({body: entity})));
                        comp.movimientoApp = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.update).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'movimientoAppListModification', content: 'OK'});
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    })
                )
            );

            it('Should call create service on save for new entity',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        const entity = new MovimientoApp();
                        spyOn(service, 'create').and.returnValue(Observable.of(new HttpResponse({body: entity})));
                        comp.movimientoApp = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.create).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'movimientoAppListModification', content: 'OK'});
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    })
                )
            );
        });
    });

});
