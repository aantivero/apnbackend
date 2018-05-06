import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { UserExtra } from './user-extra.model';
import { UserExtraPopupService } from './user-extra-popup.service';
import { UserExtraService } from './user-extra.service';

@Component({
    selector: 'jhi-user-extra-delete-dialog',
    templateUrl: './user-extra-delete-dialog.component.html'
})
export class UserExtraDeleteDialogComponent {

    userExtra: UserExtra;

    constructor(
        private userExtraService: UserExtraService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.userExtraService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'userExtraListModification',
                content: 'Deleted an userExtra'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-user-extra-delete-popup',
    template: ''
})
export class UserExtraDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private userExtraPopupService: UserExtraPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.userExtraPopupService
                .open(UserExtraDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
