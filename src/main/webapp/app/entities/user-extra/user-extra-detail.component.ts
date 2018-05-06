import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager } from 'ng-jhipster';

import { UserExtra } from './user-extra.model';
import { UserExtraService } from './user-extra.service';

@Component({
    selector: 'jhi-user-extra-detail',
    templateUrl: './user-extra-detail.component.html'
})
export class UserExtraDetailComponent implements OnInit, OnDestroy {

    userExtra: UserExtra;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private userExtraService: UserExtraService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInUserExtras();
    }

    load(id) {
        this.userExtraService.find(id)
            .subscribe((userExtraResponse: HttpResponse<UserExtra>) => {
                this.userExtra = userExtraResponse.body;
            });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInUserExtras() {
        this.eventSubscriber = this.eventManager.subscribe(
            'userExtraListModification',
            (response) => this.load(this.userExtra.id)
        );
    }
}
