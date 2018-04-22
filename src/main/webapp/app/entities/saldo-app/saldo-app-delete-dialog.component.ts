import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { SaldoApp } from './saldo-app.model';
import { SaldoAppPopupService } from './saldo-app-popup.service';
import { SaldoAppService } from './saldo-app.service';

@Component({
    selector: 'jhi-saldo-app-delete-dialog',
    templateUrl: './saldo-app-delete-dialog.component.html'
})
export class SaldoAppDeleteDialogComponent {

    saldoApp: SaldoApp;

    constructor(
        private saldoAppService: SaldoAppService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.saldoAppService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'saldoAppListModification',
                content: 'Deleted an saldoApp'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-saldo-app-delete-popup',
    template: ''
})
export class SaldoAppDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private saldoAppPopupService: SaldoAppPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.saldoAppPopupService
                .open(SaldoAppDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
