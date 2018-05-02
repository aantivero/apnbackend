import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { MovimientoApp } from './movimiento-app.model';
import { MovimientoAppPopupService } from './movimiento-app-popup.service';
import { MovimientoAppService } from './movimiento-app.service';
import { Banco, BancoService } from '../banco';

@Component({
    selector: 'jhi-movimiento-app-dialog',
    templateUrl: './movimiento-app-dialog.component.html'
})
export class MovimientoAppDialogComponent implements OnInit {

    movimientoApp: MovimientoApp;
    isSaving: boolean;

    bancos: Banco[];

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private movimientoAppService: MovimientoAppService,
        private bancoService: BancoService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.bancoService.query()
            .subscribe((res: HttpResponse<Banco[]>) => { this.bancos = res.body; }, (res: HttpErrorResponse) => this.onError(res.message));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.movimientoApp.id !== undefined) {
            this.subscribeToSaveResponse(
                this.movimientoAppService.update(this.movimientoApp));
        } else {
            this.subscribeToSaveResponse(
                this.movimientoAppService.create(this.movimientoApp));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<MovimientoApp>>) {
        result.subscribe((res: HttpResponse<MovimientoApp>) =>
            this.onSaveSuccess(res.body), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess(result: MovimientoApp) {
        this.eventManager.broadcast({ name: 'movimientoAppListModification', content: 'OK'});
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
}

@Component({
    selector: 'jhi-movimiento-app-popup',
    template: ''
})
export class MovimientoAppPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private movimientoAppPopupService: MovimientoAppPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.movimientoAppPopupService
                    .open(MovimientoAppDialogComponent as Component, params['id']);
            } else {
                this.movimientoAppPopupService
                    .open(MovimientoAppDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
