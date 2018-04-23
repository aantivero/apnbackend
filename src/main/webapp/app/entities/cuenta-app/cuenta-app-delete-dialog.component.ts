import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { CuentaApp } from './cuenta-app.model';
import { CuentaAppPopupService } from './cuenta-app-popup.service';
import { CuentaAppService } from './cuenta-app.service';

@Component({
    selector: 'jhi-cuenta-app-delete-dialog',
    templateUrl: './cuenta-app-delete-dialog.component.html'
})
export class CuentaAppDeleteDialogComponent {

    cuentaApp: CuentaApp;

    constructor(
        private cuentaAppService: CuentaAppService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.cuentaAppService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'cuentaAppListModification',
                content: 'Deleted an cuentaApp'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-cuenta-app-delete-popup',
    template: ''
})
export class CuentaAppDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private cuentaAppPopupService: CuentaAppPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.cuentaAppPopupService
                .open(CuentaAppDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
