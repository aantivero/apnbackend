import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Cuenta } from './cuenta.model';
import { CuentaPopupService } from './cuenta-popup.service';
import { CuentaService } from './cuenta.service';

@Component({
    selector: 'jhi-cuenta-delete-dialog',
    templateUrl: './cuenta-delete-dialog.component.html'
})
export class CuentaDeleteDialogComponent {

    cuenta: Cuenta;

    constructor(
        private cuentaService: CuentaService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.cuentaService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'cuentaListModification',
                content: 'Deleted an cuenta'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-cuenta-delete-popup',
    template: ''
})
export class CuentaDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private cuentaPopupService: CuentaPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.cuentaPopupService
                .open(CuentaDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
