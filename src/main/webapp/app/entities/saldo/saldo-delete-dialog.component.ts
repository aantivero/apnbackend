import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Saldo } from './saldo.model';
import { SaldoPopupService } from './saldo-popup.service';
import { SaldoService } from './saldo.service';

@Component({
    selector: 'jhi-saldo-delete-dialog',
    templateUrl: './saldo-delete-dialog.component.html'
})
export class SaldoDeleteDialogComponent {

    saldo: Saldo;

    constructor(
        private saldoService: SaldoService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.saldoService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'saldoListModification',
                content: 'Deleted an saldo'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-saldo-delete-popup',
    template: ''
})
export class SaldoDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private saldoPopupService: SaldoPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.saldoPopupService
                .open(SaldoDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
