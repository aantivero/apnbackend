import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil } from 'ng-jhipster';

import { UserRouteAccessService } from '../../shared';
import { CargarSaldoComponent } from './cargar-saldo.component';
import { CargarSaldoDetailComponent } from './cargar-saldo-detail.component';
import { CargarSaldoPopupComponent } from './cargar-saldo-dialog.component';
import { CargarSaldoDeletePopupComponent } from './cargar-saldo-delete-dialog.component';

@Injectable()
export class CargarSaldoResolvePagingParams implements Resolve<any> {

    constructor(private paginationUtil: JhiPaginationUtil) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const page = route.queryParams['page'] ? route.queryParams['page'] : '1';
        const sort = route.queryParams['sort'] ? route.queryParams['sort'] : 'id,asc';
        return {
            page: this.paginationUtil.parsePage(page),
            predicate: this.paginationUtil.parsePredicate(sort),
            ascending: this.paginationUtil.parseAscending(sort)
      };
    }
}

export const cargarSaldoRoute: Routes = [
    {
        path: 'cargar-saldo',
        component: CargarSaldoComponent,
        resolve: {
            'pagingParams': CargarSaldoResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'paynowApp.cargarSaldo.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'cargar-saldo/:id',
        component: CargarSaldoDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'paynowApp.cargarSaldo.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const cargarSaldoPopupRoute: Routes = [
    {
        path: 'cargar-saldo-new',
        component: CargarSaldoPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'paynowApp.cargarSaldo.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'cargar-saldo/:id/edit',
        component: CargarSaldoPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'paynowApp.cargarSaldo.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'cargar-saldo/:id/delete',
        component: CargarSaldoDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'paynowApp.cargarSaldo.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
