import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Cuenta } from './cuenta.model';
import { CuentaPopupService } from './cuenta-popup.service';
import { CuentaService } from './cuenta.service';
import { User, UserService } from '../../shared';
import { Banco, BancoService } from '../banco';

@Component({
    selector: 'jhi-cuenta-dialog',
    templateUrl: './cuenta-dialog.component.html'
})
export class CuentaDialogComponent implements OnInit {

    cuenta: Cuenta;
    isSaving: boolean;

    users: User[];

    bancos: Banco[];

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private cuentaService: CuentaService,
        private userService: UserService,
        private bancoService: BancoService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.userService.query()
            .subscribe((res: HttpResponse<User[]>) => { this.users = res.body; }, (res: HttpErrorResponse) => this.onError(res.message));
        this.bancoService.query()
            .subscribe((res: HttpResponse<Banco[]>) => { this.bancos = res.body; }, (res: HttpErrorResponse) => this.onError(res.message));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.cuenta.id !== undefined) {
            this.subscribeToSaveResponse(
                this.cuentaService.update(this.cuenta));
        } else {
            this.subscribeToSaveResponse(
                this.cuentaService.create(this.cuenta));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<Cuenta>>) {
        result.subscribe((res: HttpResponse<Cuenta>) =>
            this.onSaveSuccess(res.body), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess(result: Cuenta) {
        this.eventManager.broadcast({ name: 'cuentaListModification', content: 'OK'});
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

    trackBancoById(index: number, item: Banco) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-cuenta-popup',
    template: ''
})
export class CuentaPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private cuentaPopupService: CuentaPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.cuentaPopupService
                    .open(CuentaDialogComponent as Component, params['id']);
            } else {
                this.cuentaPopupService
                    .open(CuentaDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
