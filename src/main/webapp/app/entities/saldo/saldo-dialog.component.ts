import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Saldo } from './saldo.model';
import { SaldoPopupService } from './saldo-popup.service';
import { SaldoService } from './saldo.service';
import { User, UserService } from '../../shared';
import { App, AppService } from '../app';

@Component({
    selector: 'jhi-saldo-dialog',
    templateUrl: './saldo-dialog.component.html'
})
export class SaldoDialogComponent implements OnInit {

    saldo: Saldo;
    isSaving: boolean;

    users: User[];

    apps: App[];

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private saldoService: SaldoService,
        private userService: UserService,
        private appService: AppService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.userService.query()
            .subscribe((res: HttpResponse<User[]>) => { this.users = res.body; }, (res: HttpErrorResponse) => this.onError(res.message));
        this.appService.query()
            .subscribe((res: HttpResponse<App[]>) => { this.apps = res.body; }, (res: HttpErrorResponse) => this.onError(res.message));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.saldo.id !== undefined) {
            this.subscribeToSaveResponse(
                this.saldoService.update(this.saldo));
        } else {
            this.subscribeToSaveResponse(
                this.saldoService.create(this.saldo));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<Saldo>>) {
        result.subscribe((res: HttpResponse<Saldo>) =>
            this.onSaveSuccess(res.body), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess(result: Saldo) {
        this.eventManager.broadcast({ name: 'saldoListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }

    trackUserById(index: number, item: User) {
        return item.id;
    }

    trackAppById(index: number, item: App) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-saldo-popup',
    template: ''
})
export class SaldoPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private saldoPopupService: SaldoPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.saldoPopupService
                    .open(SaldoDialogComponent as Component, params['id']);
            } else {
                this.saldoPopupService
                    .open(SaldoDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
