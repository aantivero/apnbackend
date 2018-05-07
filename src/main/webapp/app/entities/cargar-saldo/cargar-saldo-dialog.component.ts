import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { CargarSaldo } from './cargar-saldo.model';
import { CargarSaldoPopupService } from './cargar-saldo-popup.service';
import { CargarSaldoService } from './cargar-saldo.service';
import { User, UserService } from '../../shared';
import { Cuenta, CuentaService } from '../cuenta';
import { TransferenciaApp, TransferenciaAppService } from '../transferencia-app';

@Component({
    selector: 'jhi-cargar-saldo-dialog',
    templateUrl: './cargar-saldo-dialog.component.html'
})
export class CargarSaldoDialogComponent implements OnInit {

    cargarSaldo: CargarSaldo;
    isSaving: boolean;

    users: User[];

    cuentas: Cuenta[];

    transferenciaapps: TransferenciaApp[];

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private cargarSaldoService: CargarSaldoService,
        private userService: UserService,
        private cuentaService: CuentaService,
        private transferenciaAppService: TransferenciaAppService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.userService.query()
            .subscribe((res: HttpResponse<User[]>) => { this.users = res.body; }, (res: HttpErrorResponse) => this.onError(res.message));
        this.cuentaService.query({'tipo.in': 'BANCARIA'})
            .subscribe((res: HttpResponse<Cuenta[]>) => { this.cuentas = res.body; }, (res: HttpErrorResponse) => this.onError(res.message));
        this.transferenciaAppService
            .query({filter: 'cargarsaldo-is-null'})
            .subscribe((res: HttpResponse<TransferenciaApp[]>) => {
                if (!this.cargarSaldo.transferenciaApp || !this.cargarSaldo.transferenciaApp.id) {
                    this.transferenciaapps = res.body;
                } else {
                    this.transferenciaAppService
                        .find(this.cargarSaldo.transferenciaApp.id)
                        .subscribe((subRes: HttpResponse<TransferenciaApp>) => {
                            this.transferenciaapps = [subRes.body].concat(res.body);
                        }, (subRes: HttpErrorResponse) => this.onError(subRes.message));
                }
            }, (res: HttpErrorResponse) => this.onError(res.message));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.cargarSaldo.id !== undefined) {
            this.subscribeToSaveResponse(
                this.cargarSaldoService.update(this.cargarSaldo));
        } else {
            this.subscribeToSaveResponse(
                this.cargarSaldoService.create(this.cargarSaldo));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<CargarSaldo>>) {
        result.subscribe((res: HttpResponse<CargarSaldo>) =>
            this.onSaveSuccess(res.body), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess(result: CargarSaldo) {
        this.eventManager.broadcast({ name: 'cargarSaldoListModification', content: 'OK'});
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

    trackCuentaById(index: number, item: Cuenta) {
        return item.id;
    }

    trackTransferenciaAppById(index: number, item: TransferenciaApp) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-cargar-saldo-popup',
    template: ''
})
export class CargarSaldoPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private cargarSaldoPopupService: CargarSaldoPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.cargarSaldoPopupService
                    .open(CargarSaldoDialogComponent as Component, params['id']);
            } else {
                this.cargarSaldoPopupService
                    .open(CargarSaldoDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
