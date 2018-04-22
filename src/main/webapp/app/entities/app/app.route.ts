import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil } from 'ng-jhipster';

import { UserRouteAccessService } from '../../shared';
import { AppComponent } from './app.component';
import { AppDetailComponent } from './app-detail.component';
import { AppPopupComponent } from './app-dialog.component';
import { AppDeletePopupComponent } from './app-delete-dialog.component';

@Injectable()
export class AppResolvePagingParams implements Resolve<any> {

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

export const appRoute: Routes = [
    {
        path: 'app',
        component: AppComponent,
        resolve: {
            'pagingParams': AppResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'paynowApp.app.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'app/:id',
        component: AppDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'paynowApp.app.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const appPopupRoute: Routes = [
    {
        path: 'app-new',
        component: AppPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'paynowApp.app.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'app/:id/edit',
        component: AppPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'paynowApp.app.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'app/:id/delete',
        component: AppDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'paynowApp.app.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
