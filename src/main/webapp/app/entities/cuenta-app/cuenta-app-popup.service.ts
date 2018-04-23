import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { HttpResponse } from '@angular/common/http';
import { CuentaApp } from './cuenta-app.model';
import { CuentaAppService } from './cuenta-app.service';

@Injectable()
export class CuentaAppPopupService {
    private ngbModalRef: NgbModalRef;

    constructor(
        private modalService: NgbModal,
        private router: Router,
        private cuentaAppService: CuentaAppService

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
                this.cuentaAppService.find(id)
                    .subscribe((cuentaAppResponse: HttpResponse<CuentaApp>) => {
                        const cuentaApp: CuentaApp = cuentaAppResponse.body;
                        this.ngbModalRef = this.cuentaAppModalRef(component, cuentaApp);
                        resolve(this.ngbModalRef);
                    });
            } else {
                // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
                setTimeout(() => {
                    this.ngbModalRef = this.cuentaAppModalRef(component, new CuentaApp());
                    resolve(this.ngbModalRef);
                }, 0);
            }
        });
    }

    cuentaAppModalRef(component: Component, cuentaApp: CuentaApp): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.cuentaApp = cuentaApp;
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
