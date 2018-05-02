import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { MovimientoApp } from './movimiento-app.model';
import { MovimientoAppPopupService } from './movimiento-app-popup.service';
import { MovimientoAppService } from './movimiento-app.service';

@Component({
    selector: 'jhi-movimiento-app-delete-dialog',
    templateUrl: './movimiento-app-delete-dialog.component.html'
})
export class MovimientoAppDeleteDialogComponent {

    movimientoApp: MovimientoApp;

    constructor(
        private movimientoAppService: MovimientoAppService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.movimientoAppService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'movimientoAppListModification',
                content: 'Deleted an movimientoApp'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-movimiento-app-delete-popup',
    template: ''
})
export class MovimientoAppDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private movimientoAppPopupService: MovimientoAppPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.movimientoAppPopupService
                .open(MovimientoAppDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
