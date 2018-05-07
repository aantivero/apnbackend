import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil } from 'ng-jhipster';

import { UserRouteAccessService } from '../../shared';
import { CuentaComponent } from './cuenta.component';
import { CuentaDetailComponent } from './cuenta-detail.component';
import { CuentaPopupComponent } from './cuenta-dialog.component';
import { CuentaDeletePopupComponent } from './cuenta-delete-dialog.component';

@Injectable()
export class CuentaResolvePagingParams implements Resolve<any> {

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

export const cuentaRoute: Routes = [
    {
        path: 'cuenta',
        component: CuentaComponent,
        resolve: {
            'pagingParams': CuentaResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'paynowApp.cuenta.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'cuenta/:id',
        component: CuentaDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'paynowApp.cuenta.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const cuentaPopupRoute: Routes = [
    {
        path: 'cuenta-new',
        component: CuentaPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'paynowApp.cuenta.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'cuenta/:id/edit',
        component: CuentaPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'paynowApp.cuenta.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'cuenta/:id/delete',
        component: CuentaDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'paynowApp.cuenta.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
