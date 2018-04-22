import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil } from 'ng-jhipster';

import { UserRouteAccessService } from '../../shared';
import { BancoComponent } from './banco.component';
import { BancoDetailComponent } from './banco-detail.component';
import { BancoPopupComponent } from './banco-dialog.component';
import { BancoDeletePopupComponent } from './banco-delete-dialog.component';

@Injectable()
export class BancoResolvePagingParams implements Resolve<any> {

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

export const bancoRoute: Routes = [
    {
        path: 'banco',
        component: BancoComponent,
        resolve: {
            'pagingParams': BancoResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'paynowApp.banco.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'banco/:id',
        component: BancoDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'paynowApp.banco.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const bancoPopupRoute: Routes = [
    {
        path: 'banco-new',
        component: BancoPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'paynowApp.banco.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'banco/:id/edit',
        component: BancoPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'paynowApp.banco.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'banco/:id/delete',
        component: BancoDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'paynowApp.banco.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
