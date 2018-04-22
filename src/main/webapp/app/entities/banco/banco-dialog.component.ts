import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Banco } from './banco.model';
import { BancoPopupService } from './banco-popup.service';
import { BancoService } from './banco.service';

@Component({
    selector: 'jhi-banco-dialog',
    templateUrl: './banco-dialog.component.html'
})
export class BancoDialogComponent implements OnInit {

    banco: Banco;
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private bancoService: BancoService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.banco.id !== undefined) {
            this.subscribeToSaveResponse(
                this.bancoService.update(this.banco));
        } else {
            this.subscribeToSaveResponse(
                this.bancoService.create(this.banco));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<Banco>>) {
        result.subscribe((res: HttpResponse<Banco>) =>
            this.onSaveSuccess(res.body), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess(result: Banco) {
        this.eventManager.broadcast({ name: 'bancoListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }
}

@Component({
    selector: 'jhi-banco-popup',
    template: ''
})
export class BancoPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private bancoPopupService: BancoPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.bancoPopupService
                    .open(BancoDialogComponent as Component, params['id']);
            } else {
                this.bancoPopupService
                    .open(BancoDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
