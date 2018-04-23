import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { CuentaApp } from './cuenta-app.model';
import { CuentaAppPopupService } from './cuenta-app-popup.service';
import { CuentaAppService } from './cuenta-app.service';
import { Banco, BancoService } from '../banco';
import { App, AppService } from '../app';

@Component({
    selector: 'jhi-cuenta-app-dialog',
    templateUrl: './cuenta-app-dialog.component.html'
})
export class CuentaAppDialogComponent implements OnInit {

    cuentaApp: CuentaApp;
    isSaving: boolean;

    bancos: Banco[];

    apps: App[];

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private cuentaAppService: CuentaAppService,
        private bancoService: BancoService,
        private appService: AppService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.bancoService.query()
            .subscribe((res: HttpResponse<Banco[]>) => { this.bancos = res.body; }, (res: HttpErrorResponse) => this.onError(res.message));
        this.appService.query()
            .subscribe((res: HttpResponse<App[]>) => { this.apps = res.body; }, (res: HttpErrorResponse) => this.onError(res.message));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.cuentaApp.id !== undefined) {
            this.subscribeToSaveResponse(
                this.cuentaAppService.update(this.cuentaApp));
        } else {
            this.subscribeToSaveResponse(
                this.cuentaAppService.create(this.cuentaApp));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<CuentaApp>>) {
        result.subscribe((res: HttpResponse<CuentaApp>) =>
            this.onSaveSuccess(res.body), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess(result: CuentaApp) {
        this.eventManager.broadcast({ name: 'cuentaAppListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }

    trackBancoById(index: number, item: Banco) {
        return item.id;
    }

    trackAppById(index: number, item: App) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-cuenta-app-popup',
    template: ''
})
export class CuentaAppPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private cuentaAppPopupService: CuentaAppPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.cuentaAppPopupService
                    .open(CuentaAppDialogComponent as Component, params['id']);
            } else {
                this.cuentaAppPopupService
                    .open(CuentaAppDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
