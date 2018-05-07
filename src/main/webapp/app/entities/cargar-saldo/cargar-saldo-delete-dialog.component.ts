import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { CargarSaldo } from './cargar-saldo.model';
import { CargarSaldoPopupService } from './cargar-saldo-popup.service';
import { CargarSaldoService } from './cargar-saldo.service';

@Component({
    selector: 'jhi-cargar-saldo-delete-dialog',
    templateUrl: './cargar-saldo-delete-dialog.component.html'
})
export class CargarSaldoDeleteDialogComponent {

    cargarSaldo: CargarSaldo;

    constructor(
        private cargarSaldoService: CargarSaldoService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.cargarSaldoService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'cargarSaldoListModification',
                content: 'Deleted an cargarSaldo'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-cargar-saldo-delete-popup',
    template: ''
})
export class CargarSaldoDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private cargarSaldoPopupService: CargarSaldoPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.cargarSaldoPopupService
                .open(CargarSaldoDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
