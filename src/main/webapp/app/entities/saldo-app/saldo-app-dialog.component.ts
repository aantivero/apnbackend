import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { SaldoApp } from './saldo-app.model';
import { SaldoAppPopupService } from './saldo-app-popup.service';
import { SaldoAppService } from './saldo-app.service';
import { App, AppService } from '../app';

@Component({
    selector: 'jhi-saldo-app-dialog',
    templateUrl: './saldo-app-dialog.component.html'
})
export class SaldoAppDialogComponent implements OnInit {

    saldoApp: SaldoApp;
    isSaving: boolean;

    apps: App[];

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private saldoAppService: SaldoAppService,
        private appService: AppService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.appService.query()
            .subscribe((res: HttpResponse<App[]>) => { this.apps = res.body; }, (res: HttpErrorResponse) => this.onError(res.message));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.saldoApp.id !== undefined) {
            this.subscribeToSaveResponse(
                this.saldoAppService.update(this.saldoApp));
        } else {
            this.subscribeToSaveResponse(
                this.saldoAppService.create(this.saldoApp));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<SaldoApp>>) {
        result.subscribe((res: HttpResponse<SaldoApp>) =>
            this.onSaveSuccess(res.body), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess(result: SaldoApp) {
        this.eventManager.broadcast({ name: 'saldoAppListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }

    trackAppById(index: number, item: App) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-saldo-app-popup',
    template: ''
})
export class SaldoAppPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private saldoAppPopupService: SaldoAppPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.saldoAppPopupService
                    .open(SaldoAppDialogComponent as Component, params['id']);
            } else {
                this.saldoAppPopupService
                    .open(SaldoAppDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
