/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable } from 'rxjs/Observable';
import { JhiEventManager } from 'ng-jhipster';

import { PaynowTestModule } from '../../../test.module';
import { CargarSaldoDeleteDialogComponent } from '../../../../../../main/webapp/app/entities/cargar-saldo/cargar-saldo-delete-dialog.component';
import { CargarSaldoService } from '../../../../../../main/webapp/app/entities/cargar-saldo/cargar-saldo.service';

describe('Component Tests', () => {

    describe('CargarSaldo Management Delete Component', () => {
        let comp: CargarSaldoDeleteDialogComponent;
        let fixture: ComponentFixture<CargarSaldoDeleteDialogComponent>;
        let service: CargarSaldoService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [PaynowTestModule],
                declarations: [CargarSaldoDeleteDialogComponent],
                providers: [
                    CargarSaldoService
                ]
            })
            .overrideTemplate(CargarSaldoDeleteDialogComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(CargarSaldoDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(CargarSaldoService);
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
