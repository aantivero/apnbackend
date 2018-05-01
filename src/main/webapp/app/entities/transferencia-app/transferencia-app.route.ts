import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil } from 'ng-jhipster';

import { UserRouteAccessService } from '../../shared';
import { TransferenciaAppComponent } from './transferencia-app.component';
import { TransferenciaAppDetailComponent } from './transferencia-app-detail.component';
import { TransferenciaAppPopupComponent } from './transferencia-app-dialog.component';
import { TransferenciaAppDeletePopupComponent } from './transferencia-app-delete-dialog.component';

@Injectable()
export class TransferenciaAppResolvePagingParams implements Resolve<any> {

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

export const transferenciaAppRoute: Routes = [
    {
        path: 'transferencia-app',
        component: TransferenciaAppComponent,
        resolve: {
            'pagingParams': TransferenciaAppResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'paynowApp.transferenciaApp.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'transferencia-app/:id',
        component: TransferenciaAppDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'paynowApp.transferenciaApp.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const transferenciaAppPopupRoute: Routes = [
    {
        path: 'transferencia-app-new',
        component: TransferenciaAppPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'paynowApp.transferenciaApp.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'transferencia-app/:id/edit',
        component: TransferenciaAppPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'paynowApp.transferenciaApp.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'transferencia-app/:id/delete',
        component: TransferenciaAppDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'paynowApp.transferenciaApp.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
