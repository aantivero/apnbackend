import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { HttpResponse } from '@angular/common/http';
import { SaldoApp } from './saldo-app.model';
import { SaldoAppService } from './saldo-app.service';

@Injectable()
export class SaldoAppPopupService {
    private ngbModalRef: NgbModalRef;

    constructor(
        private modalService: NgbModal,
        private router: Router,
        private saldoAppService: SaldoAppService

    ) {
        this.ngbModalRef = null;
    }

    open(component: Component, id?: number | any): Promise<NgbModalRef> {
        return new Promise<NgbModalRef>((resolve, reject) => {
            const isOpen = this.ngbModalRef !== null;
            if (isOpen) {
                resolve(this.ngbModalRef);
            }

            if (id) {
                this.saldoAppService.find(id)
                    .subscribe((saldoAppResponse: HttpResponse<SaldoApp>) => {
                        const saldoApp: SaldoApp = saldoAppResponse.body;
                        this.ngbModalRef = this.saldoAppModalRef(component, saldoApp);
                        resolve(this.ngbModalRef);
                    });
            } else {
                // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
                setTimeout(() => {
                    this.ngbModalRef = this.saldoAppModalRef(component, new SaldoApp());
                    resolve(this.ngbModalRef);
                }, 0);
            }
        });
    }

    saldoAppModalRef(component: Component, saldoApp: SaldoApp): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.saldoApp = saldoApp;
        modalRef.result.then((result) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true, queryParamsHandling: 'merge' });
            this.ngbModalRef = null;
        }, (reason) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true, queryParamsHandling: 'merge' });
            this.ngbModalRef = null;
        });
        return modalRef;
    }
}
