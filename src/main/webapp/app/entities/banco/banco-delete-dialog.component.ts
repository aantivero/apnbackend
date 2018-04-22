import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Banco } from './banco.model';
import { BancoPopupService } from './banco-popup.service';
import { BancoService } from './banco.service';

@Component({
    selector: 'jhi-banco-delete-dialog',
    templateUrl: './banco-delete-dialog.component.html'
})
export class BancoDeleteDialogComponent {

    banco: Banco;

    constructor(
        private bancoService: BancoService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.bancoService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'bancoListModification',
                content: 'Deleted an banco'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-banco-delete-popup',
    template: ''
})
export class BancoDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private bancoPopupService: BancoPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.bancoPopupService
                .open(BancoDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
