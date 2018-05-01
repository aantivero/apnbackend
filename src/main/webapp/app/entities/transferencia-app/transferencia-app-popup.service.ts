import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { HttpResponse } from '@angular/common/http';
import { DatePipe } from '@angular/common';
import { TransferenciaApp } from './transferencia-app.model';
import { TransferenciaAppService } from './transferencia-app.service';

@Injectable()
export class TransferenciaAppPopupService {
    private ngbModalRef: NgbModalRef;

    constructor(
        private datePipe: DatePipe,
        private modalService: NgbModal,
        private router: Router,
        private transferenciaAppService: TransferenciaAppService

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
                this.transferenciaAppService.find(id)
                    .subscribe((transferenciaAppResponse: HttpResponse<TransferenciaApp>) => {
                        const transferenciaApp: TransferenciaApp = transferenciaAppResponse.body;
                        transferenciaApp.timestamp = this.datePipe
                            .transform(transferenciaApp.timestamp, 'yyyy-MM-ddTHH:mm:ss');
                        this.ngbModalRef = this.transferenciaAppModalRef(component, transferenciaApp);
                        resolve(this.ngbModalRef);
                    });
            } else {
                // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
                setTimeout(() => {
                    this.ngbModalRef = this.transferenciaAppModalRef(component, new TransferenciaApp());
                    resolve(this.ngbModalRef);
                }, 0);
            }
        });
    }

    transferenciaAppModalRef(component: Component, transferenciaApp: TransferenciaApp): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.transferenciaApp = transferenciaApp;
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
