import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { HttpResponse } from '@angular/common/http';
import { DatePipe } from '@angular/common';
import { Cuenta } from './cuenta.model';
import { CuentaService } from './cuenta.service';

@Injectable()
export class CuentaPopupService {
    private ngbModalRef: NgbModalRef;

    constructor(
        private datePipe: DatePipe,
        private modalService: NgbModal,
        private router: Router,
        private cuentaService: CuentaService

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
                this.cuentaService.find(id)
                    .subscribe((cuentaResponse: HttpResponse<Cuenta>) => {
                        const cuenta: Cuenta = cuentaResponse.body;
                        cuenta.fechaCreacion = this.datePipe
                            .transform(cuenta.fechaCreacion, 'yyyy-MM-ddTHH:mm:ss');
                        cuenta.fechaModificacion = this.datePipe
                            .transform(cuenta.fechaModificacion, 'yyyy-MM-ddTHH:mm:ss');
                        this.ngbModalRef = this.cuentaModalRef(component, cuenta);
                        resolve(this.ngbModalRef);
                    });
            } else {
                // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
                setTimeout(() => {
                    this.ngbModalRef = this.cuentaModalRef(component, new Cuenta());
                    resolve(this.ngbModalRef);
                }, 0);
            }
        });
    }

    cuentaModalRef(component: Component, cuenta: Cuenta): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.cuenta = cuenta;
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
