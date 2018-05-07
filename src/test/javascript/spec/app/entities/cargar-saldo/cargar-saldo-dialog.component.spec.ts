/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable } from 'rxjs/Observable';
import { JhiEventManager } from 'ng-jhipster';

import { PaynowTestModule } from '../../../test.module';
import { CargarSaldoDialogComponent } from '../../../../../../main/webapp/app/entities/cargar-saldo/cargar-saldo-dialog.component';
import { CargarSaldoService } from '../../../../../../main/webapp/app/entities/cargar-saldo/cargar-saldo.service';
import { CargarSaldo } from '../../../../../../main/webapp/app/entities/cargar-saldo/cargar-saldo.model';
import { UserService } from '../../../../../../main/webapp/app/shared';
import { CuentaService } from '../../../../../../main/webapp/app/entities/cuenta';
import { TransferenciaAppService } from '../../../../../../main/webapp/app/entities/transferencia-app';

describe('Component Tests', () => {

    describe('CargarSaldo Management Dialog Component', () => {
        let comp: CargarSaldoDialogComponent;
        let fixture: ComponentFixture<CargarSaldoDialogComponent>;
        let service: CargarSaldoService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [PaynowTestModule],
                declarations: [CargarSaldoDialogComponent],
                providers: [
                    UserService,
                    CuentaService,
                    TransferenciaAppService,
                    CargarSaldoService
                ]
            })
            .overrideTemplate(CargarSaldoDialogComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(CargarSaldoDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(CargarSaldoService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        const entity = new CargarSaldo(123);
                        spyOn(service, 'update').and.returnValue(Observable.of(new HttpResponse({body: entity})));
                        comp.cargarSaldo = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.update).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'cargarSaldoListModification', content: 'OK'});
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    })
                )
            );

            it('Should call create service on save for new entity',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        const entity = new CargarSaldo();
                        spyOn(service, 'create').and.returnValue(Observable.of(new HttpResponse({body: entity})));
                        comp.cargarSaldo = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.create).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'cargarSaldoListModification', content: 'OK'});
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    })
                )
            );
        });
    });

});
