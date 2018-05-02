import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager } from 'ng-jhipster';

import { MovimientoApp } from './movimiento-app.model';
import { MovimientoAppService } from './movimiento-app.service';

@Component({
    selector: 'jhi-movimiento-app-detail',
    templateUrl: './movimiento-app-detail.component.html'
})
export class MovimientoAppDetailComponent implements OnInit, OnDestroy {

    movimientoApp: MovimientoApp;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private movimientoAppService: MovimientoAppService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInMovimientoApps();
    }

    load(id) {
        this.movimientoAppService.find(id)
            .subscribe((movimientoAppResponse: HttpResponse<MovimientoApp>) => {
                this.movimientoApp = movimientoAppResponse.body;
            });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInMovimientoApps() {
        this.eventSubscriber = this.eventManager.subscribe(
            'movimientoAppListModification',
            (response) => this.load(this.movimientoApp.id)
        );
    }
}
