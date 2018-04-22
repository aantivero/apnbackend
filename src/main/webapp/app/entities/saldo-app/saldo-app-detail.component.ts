import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager } from 'ng-jhipster';

import { SaldoApp } from './saldo-app.model';
import { SaldoAppService } from './saldo-app.service';

@Component({
    selector: 'jhi-saldo-app-detail',
    templateUrl: './saldo-app-detail.component.html'
})
export class SaldoAppDetailComponent implements OnInit, OnDestroy {

    saldoApp: SaldoApp;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private saldoAppService: SaldoAppService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInSaldoApps();
    }

    load(id) {
        this.saldoAppService.find(id)
            .subscribe((saldoAppResponse: HttpResponse<SaldoApp>) => {
                this.saldoApp = saldoAppResponse.body;
            });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInSaldoApps() {
        this.eventSubscriber = this.eventManager.subscribe(
            'saldoAppListModification',
            (response) => this.load(this.saldoApp.id)
        );
    }
}
