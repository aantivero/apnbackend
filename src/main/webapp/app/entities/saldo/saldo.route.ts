import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil } from 'ng-jhipster';

import { UserRouteAccessService } from '../../shared';
import { SaldoComponent } from './saldo.component';
import { SaldoDetailComponent } from './saldo-detail.component';
import { SaldoPopupComponent } from './saldo-dialog.component';
import { SaldoDeletePopupComponent } from './saldo-delete-dialog.component';

@Injectable()
export class SaldoResolvePagingParams implements Resolve<any> {

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

export const saldoRoute: Routes = [
    {
        path: 'saldo',
        component: SaldoComponent,
        resolve: {
            'pagingParams': SaldoResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'paynowApp.saldo.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'saldo/:id',
        component: SaldoDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'paynowApp.saldo.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const saldoPopupRoute: Routes = [
    {
        path: 'saldo-new',
        component: SaldoPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'paynowApp.saldo.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'saldo/:id/edit',
        component: SaldoPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'paynowApp.saldo.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'saldo/:id/delete',
        component: SaldoDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'paynowApp.saldo.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
