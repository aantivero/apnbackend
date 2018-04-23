import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil } from 'ng-jhipster';

import { UserRouteAccessService } from '../../shared';
import { CuentaAppComponent } from './cuenta-app.component';
import { CuentaAppDetailComponent } from './cuenta-app-detail.component';
import { CuentaAppPopupComponent } from './cuenta-app-dialog.component';
import { CuentaAppDeletePopupComponent } from './cuenta-app-delete-dialog.component';

@Injectable()
export class CuentaAppResolvePagingParams implements Resolve<any> {

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

export const cuentaAppRoute: Routes = [
    {
        path: 'cuenta-app',
        component: CuentaAppComponent,
        resolve: {
            'pagingParams': CuentaAppResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'paynowApp.cuentaApp.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'cuenta-app/:id',
        component: CuentaAppDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'paynowApp.cuentaApp.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const cuentaAppPopupRoute: Routes = [
    {
        path: 'cuenta-app-new',
        component: CuentaAppPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'paynowApp.cuentaApp.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'cuenta-app/:id/edit',
        component: CuentaAppPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'paynowApp.cuentaApp.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'cuenta-app/:id/delete',
        component: CuentaAppDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'paynowApp.cuentaApp.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
