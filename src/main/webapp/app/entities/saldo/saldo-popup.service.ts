import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { HttpResponse } from '@angular/common/http';
import { DatePipe } from '@angular/common';
import { Saldo } from './saldo.model';
import { SaldoService } from './saldo.service';

@Injectable()
export class SaldoPopupService {
    private ngbModalRef: NgbModalRef;

    constructor(
        private datePipe: DatePipe,
        private modalService: NgbModal,
        private router: Router,
        private saldoService: SaldoService

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
                this.saldoService.find(id)
                    .subscribe((saldoResponse: HttpResponse<Saldo>) => {
                        const saldo: Saldo = saldoResponse.body;
                        saldo.fechaCreacion = this.datePipe
                            .transform(saldo.fechaCreacion, 'yyyy-MM-ddTHH:mm:ss');
                        saldo.fechaModificacion = this.datePipe
                            .transform(saldo.fechaModificacion, 'yyyy-MM-ddTHH:mm:ss');
                        this.ngbModalRef = this.saldoModalRef(component, saldo);
                        resolve(this.ngbModalRef);
                    });
            } else {
                // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
                setTimeout(() => {
                    this.ngbModalRef = this.saldoModalRef(component, new Saldo());
                    resolve(this.ngbModalRef);
                }, 0);
            }
        });
    }

    saldoModalRef(component: Component, saldo: Saldo): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.saldo = saldo;
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
