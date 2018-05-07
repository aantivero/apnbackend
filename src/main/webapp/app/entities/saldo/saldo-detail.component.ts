import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager } from 'ng-jhipster';

import { Saldo } from './saldo.model';
import { SaldoService } from './saldo.service';

@Component({
    selector: 'jhi-saldo-detail',
    templateUrl: './saldo-detail.component.html'
})
export class SaldoDetailComponent implements OnInit, OnDestroy {

    saldo: Saldo;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private saldoService: SaldoService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInSaldos();
    }

    load(id) {
        this.saldoService.find(id)
            .subscribe((saldoResponse: HttpResponse<Saldo>) => {
                this.saldo = saldoResponse.body;
            });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInSaldos() {
        this.eventSubscriber = this.eventManager.subscribe(
            'saldoListModification',
            (response) => this.load(this.saldo.id)
        );
    }
}
