import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { TransferenciaApp } from './transferencia-app.model';
import { TransferenciaAppPopupService } from './transferencia-app-popup.service';
import { TransferenciaAppService } from './transferencia-app.service';
import { CuentaApp, CuentaAppService } from '../cuenta-app';
import { Banco, BancoService } from '../banco';

@Component({
    selector: 'jhi-transferencia-app-dialog',
    templateUrl: './transferencia-app-dialog.component.html'
})
export class TransferenciaAppDialogComponent implements OnInit {

    transferenciaApp: TransferenciaApp;
    isSaving: boolean;

    cuentaapps: CuentaApp[];

    bancos: Banco[];

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private transferenciaAppService: TransferenciaAppService,
        private cuentaAppService: CuentaAppService,
        private bancoService: BancoService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.cuentaAppService.query()
            .subscribe((res: HttpResponse<CuentaApp[]>) => { this.cuentaapps = res.body; }, (res: HttpErrorResponse) => this.onError(res.message));
        this.bancoService.query()
            .subscribe((res: HttpResponse<Banco[]>) => { this.bancos = res.body; }, (res: HttpErrorResponse) => this.onError(res.message));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.transferenciaApp.id !== undefined) {
            this.subscribeToSaveResponse(
                this.transferenciaAppService.update(this.transferenciaApp));
        } else {
            this.subscribeToSaveResponse(
                this.transferenciaAppService.create(this.transferenciaApp));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<TransferenciaApp>>) {
        result.subscribe((res: HttpResponse<TransferenciaApp>) =>
            this.onSaveSuccess(res.body), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess(result: TransferenciaApp) {
        this.eventManager.broadcast({ name: 'transferenciaAppListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }

    trackCuentaAppById(index: number, item: CuentaApp) {
        return item.id;
    }

    trackBancoById(index: number, item: Banco) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-transferencia-app-popup',
    template: ''
})
export class TransferenciaAppPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private transferenciaAppPopupService: TransferenciaAppPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.transferenciaAppPopupService
                    .open(TransferenciaAppDialogComponent as Component, params['id']);
            } else {
                this.transferenciaAppPopupService
                    .open(TransferenciaAppDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
