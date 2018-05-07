import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager } from 'ng-jhipster';

import { CargarSaldo } from './cargar-saldo.model';
import { CargarSaldoService } from './cargar-saldo.service';

@Component({
    selector: 'jhi-cargar-saldo-detail',
    templateUrl: './cargar-saldo-detail.component.html'
})
export class CargarSaldoDetailComponent implements OnInit, OnDestroy {

    cargarSaldo: CargarSaldo;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private cargarSaldoService: CargarSaldoService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInCargarSaldos();
    }

    load(id) {
        this.cargarSaldoService.find(id)
            .subscribe((cargarSaldoResponse: HttpResponse<CargarSaldo>) => {
                this.cargarSaldo = cargarSaldoResponse.body;
            });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInCargarSaldos() {
        this.eventSubscriber = this.eventManager.subscribe(
            'cargarSaldoListModification',
            (response) => this.load(this.cargarSaldo.id)
        );
    }
}
