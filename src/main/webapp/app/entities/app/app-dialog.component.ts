import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { App } from './app.model';
import { AppPopupService } from './app-popup.service';
import { AppService } from './app.service';

@Component({
    selector: 'jhi-app-dialog',
    templateUrl: './app-dialog.component.html'
})
export class AppDialogComponent implements OnInit {

    app: App;
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private appService: AppService,
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
        if (this.app.id !== undefined) {
            this.subscribeToSaveResponse(
                this.appService.update(this.app));
        } else {
            this.subscribeToSaveResponse(
                this.appService.create(this.app));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<App>>) {
        result.subscribe((res: HttpResponse<App>) =>
            this.onSaveSuccess(res.body), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess(result: App) {
        this.eventManager.broadcast({ name: 'appListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }
}

@Component({
    selector: 'jhi-app-popup',
    template: ''
})
export class AppPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private appPopupService: AppPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.appPopupService
                    .open(AppDialogComponent as Component, params['id']);
            } else {
                this.appPopupService
                    .open(AppDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
