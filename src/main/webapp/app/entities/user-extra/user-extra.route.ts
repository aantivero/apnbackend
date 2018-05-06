import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil } from 'ng-jhipster';

import { UserRouteAccessService } from '../../shared';
import { UserExtraComponent } from './user-extra.component';
import { UserExtraDetailComponent } from './user-extra-detail.component';
import { UserExtraPopupComponent } from './user-extra-dialog.component';
import { UserExtraDeletePopupComponent } from './user-extra-delete-dialog.component';

@Injectable()
export class UserExtraResolvePagingParams implements Resolve<any> {

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

export const userExtraRoute: Routes = [
    {
        path: 'user-extra',
        component: UserExtraComponent,
        resolve: {
            'pagingParams': UserExtraResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'paynowApp.userExtra.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'user-extra/:id',
        component: UserExtraDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'paynowApp.userExtra.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const userExtraPopupRoute: Routes = [
    {
        path: 'user-extra-new',
        component: UserExtraPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'paynowApp.userExtra.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'user-extra/:id/edit',
        component: UserExtraPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'paynowApp.userExtra.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'user-extra/:id/delete',
        component: UserExtraDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'paynowApp.userExtra.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
