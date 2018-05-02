import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { HttpResponse } from '@angular/common/http';
import { DatePipe } from '@angular/common';
import { MovimientoApp } from './movimiento-app.model';
import { MovimientoAppService } from './movimiento-app.service';

@Injectable()
export class MovimientoAppPopupService {
    private ngbModalRef: NgbModalRef;

    constructor(
        private datePipe: DatePipe,
        private modalService: NgbModal,
        private router: Router,
        private movimientoAppService: MovimientoAppService

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
                this.movimientoAppService.find(id)
                    .subscribe((movimientoAppResponse: HttpResponse<MovimientoApp>) => {
                        const movimientoApp: MovimientoApp = movimientoAppResponse.body;
                        movimientoApp.timestamp = this.datePipe
                            .transform(movimientoApp.timestamp, 'yyyy-MM-ddTHH:mm:ss');
                        movimientoApp.consolidadoTimestamp = this.datePipe
                            .transform(movimientoApp.consolidadoTimestamp, 'yyyy-MM-ddTHH:mm:ss');
                        this.ngbModalRef = this.movimientoAppModalRef(component, movimientoApp);
                        resolve(this.ngbModalRef);
                    });
            } else {
                // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
                setTimeout(() => {
                    this.ngbModalRef = this.movimientoAppModalRef(component, new MovimientoApp());
                    resolve(this.ngbModalRef);
                }, 0);
            }
        });
    }

    movimientoAppModalRef(component: Component, movimientoApp: MovimientoApp): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.movimientoApp = movimientoApp;
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
