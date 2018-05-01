import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager } from 'ng-jhipster';

import { TransferenciaApp } from './transferencia-app.model';
import { TransferenciaAppService } from './transferencia-app.service';

@Component({
    selector: 'jhi-transferencia-app-detail',
    templateUrl: './transferencia-app-detail.component.html'
})
export class TransferenciaAppDetailComponent implements OnInit, OnDestroy {

    transferenciaApp: TransferenciaApp;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private transferenciaAppService: TransferenciaAppService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInTransferenciaApps();
    }

    load(id) {
        this.transferenciaAppService.find(id)
            .subscribe((transferenciaAppResponse: HttpResponse<TransferenciaApp>) => {
                this.transferenciaApp = transferenciaAppResponse.body;
            });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInTransferenciaApps() {
        this.eventSubscriber = this.eventManager.subscribe(
            'transferenciaAppListModification',
            (response) => this.load(this.transferenciaApp.id)
        );
    }
}
