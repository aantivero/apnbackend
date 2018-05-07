import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { HttpResponse } from '@angular/common/http';
import { CargarSaldo } from './cargar-saldo.model';
import { CargarSaldoService } from './cargar-saldo.service';

@Injectable()
export class CargarSaldoPopupService {
    private ngbModalRef: NgbModalRef;

    constructor(
        private modalService: NgbModal,
        private router: Router,
        private cargarSaldoService: CargarSaldoService

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
                this.cargarSaldoService.find(id)
                    .subscribe((cargarSaldoResponse: HttpResponse<CargarSaldo>) => {
                        const cargarSaldo: CargarSaldo = cargarSaldoResponse.body;
                        this.ngbModalRef = this.cargarSaldoModalRef(component, cargarSaldo);
                        resolve(this.ngbModalRef);
                    });
            } else {
                // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
                setTimeout(() => {
                    this.ngbModalRef = this.cargarSaldoModalRef(component, new CargarSaldo());
                    resolve(this.ngbModalRef);
                }, 0);
            }
        });
    }

    cargarSaldoModalRef(component: Component, cargarSaldo: CargarSaldo): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.cargarSaldo = cargarSaldo;
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
