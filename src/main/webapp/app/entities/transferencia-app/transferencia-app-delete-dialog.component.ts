import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { TransferenciaApp } from './transferencia-app.model';
import { TransferenciaAppPopupService } from './transferencia-app-popup.service';
import { TransferenciaAppService } from './transferencia-app.service';

@Component({
    selector: 'jhi-transferencia-app-delete-dialog',
    templateUrl: './transferencia-app-delete-dialog.component.html'
})
export class TransferenciaAppDeleteDialogComponent {

    transferenciaApp: TransferenciaApp;

    constructor(
        private transferenciaAppService: TransferenciaAppService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.transferenciaAppService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'transferenciaAppListModification',
                content: 'Deleted an transferenciaApp'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-transferencia-app-delete-popup',
    template: ''
})
export class TransferenciaAppDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private transferenciaAppPopupService: TransferenciaAppPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.transferenciaAppPopupService
                .open(TransferenciaAppDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
