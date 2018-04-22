import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager } from 'ng-jhipster';

import { App } from './app.model';
import { AppService } from './app.service';

@Component({
    selector: 'jhi-app-detail',
    templateUrl: './app-detail.component.html'
})
export class AppDetailComponent implements OnInit, OnDestroy {

    app: App;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private appService: AppService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInApps();
    }

    load(id) {
        this.appService.find(id)
            .subscribe((appResponse: HttpResponse<App>) => {
                this.app = appResponse.body;
            });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInApps() {
        this.eventSubscriber = this.eventManager.subscribe(
            'appListModification',
            (response) => this.load(this.app.id)
        );
    }
}
