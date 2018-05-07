import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager } from 'ng-jhipster';

import { Cuenta } from './cuenta.model';
import { CuentaService } from './cuenta.service';

@Component({
    selector: 'jhi-cuenta-detail',
    templateUrl: './cuenta-detail.component.html'
})
export class CuentaDetailComponent implements OnInit, OnDestroy {

    cuenta: Cuenta;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private cuentaService: CuentaService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInCuentas();
    }

    load(id) {
        this.cuentaService.find(id)
            .subscribe((cuentaResponse: HttpResponse<Cuenta>) => {
                this.cuenta = cuentaResponse.body;
            });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInCuentas() {
        this.eventSubscriber = this.eventManager.subscribe(
            'cuentaListModification',
            (response) => this.load(this.cuenta.id)
        );
    }
}
