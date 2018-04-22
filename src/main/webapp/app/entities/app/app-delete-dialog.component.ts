import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { App } from './app.model';
import { AppPopupService } from './app-popup.service';
import { AppService } from './app.service';

@Component({
    selector: 'jhi-app-delete-dialog',
    templateUrl: './app-delete-dialog.component.html'
})
export class AppDeleteDialogComponent {

    app: App;

    constructor(
        private appService: AppService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.appService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'appListModification',
                content: 'Deleted an app'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-app-delete-popup',
    template: ''
})
export class AppDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private appPopupService: AppPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.appPopupService
                .open(AppDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
