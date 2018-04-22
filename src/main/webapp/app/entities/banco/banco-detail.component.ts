import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager } from 'ng-jhipster';

import { Banco } from './banco.model';
import { BancoService } from './banco.service';

@Component({
    selector: 'jhi-banco-detail',
    templateUrl: './banco-detail.component.html'
})
export class BancoDetailComponent implements OnInit, OnDestroy {

    banco: Banco;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private bancoService: BancoService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInBancos();
    }

    load(id) {
        this.bancoService.find(id)
            .subscribe((bancoResponse: HttpResponse<Banco>) => {
                this.banco = bancoResponse.body;
            });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInBancos() {
        this.eventSubscriber = this.eventManager.subscribe(
            'bancoListModification',
            (response) => this.load(this.banco.id)
        );
    }
}
