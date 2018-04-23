import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager } from 'ng-jhipster';

import { CuentaApp } from './cuenta-app.model';
import { CuentaAppService } from './cuenta-app.service';

@Component({
    selector: 'jhi-cuenta-app-detail',
    templateUrl: './cuenta-app-detail.component.html'
})
export class CuentaAppDetailComponent implements OnInit, OnDestroy {

    cuentaApp: CuentaApp;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private cuentaAppService: CuentaAppService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInCuentaApps();
    }

    load(id) {
        this.cuentaAppService.find(id)
            .subscribe((cuentaAppResponse: HttpResponse<CuentaApp>) => {
                this.cuentaApp = cuentaAppResponse.body;
            });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInCuentaApps() {
        this.eventSubscriber = this.eventManager.subscribe(
            'cuentaAppListModification',
            (response) => this.load(this.cuentaApp.id)
        );
    }
}
