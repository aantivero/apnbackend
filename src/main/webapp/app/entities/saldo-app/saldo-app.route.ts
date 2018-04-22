import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil } from 'ng-jhipster';

import { UserRouteAccessService } from '../../shared';
import { SaldoAppComponent } from './saldo-app.component';
import { SaldoAppDetailComponent } from './saldo-app-detail.component';
import { SaldoAppPopupComponent } from './saldo-app-dialog.component';
import { SaldoAppDeletePopupComponent } from './saldo-app-delete-dialog.component';

@Injectable()
export class SaldoAppResolvePagingParams implements Resolve<any> {

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

export const saldoAppRoute: Routes = [
    {
        path: 'saldo-app',
        component: SaldoAppComponent,
        resolve: {
            'pagingParams': SaldoAppResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'paynowApp.saldoApp.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'saldo-app/:id',
        component: SaldoAppDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'paynowApp.saldoApp.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const saldoAppPopupRoute: Routes = [
    {
        path: 'saldo-app-new',
        component: SaldoAppPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'paynowApp.saldoApp.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'saldo-app/:id/edit',
        component: SaldoAppPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'paynowApp.saldoApp.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'saldo-app/:id/delete',
        component: SaldoAppDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'paynowApp.saldoApp.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
