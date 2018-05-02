import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil } from 'ng-jhipster';

import { UserRouteAccessService } from '../../shared';
import { MovimientoAppComponent } from './movimiento-app.component';
import { MovimientoAppDetailComponent } from './movimiento-app-detail.component';
import { MovimientoAppPopupComponent } from './movimiento-app-dialog.component';
import { MovimientoAppDeletePopupComponent } from './movimiento-app-delete-dialog.component';

@Injectable()
export class MovimientoAppResolvePagingParams implements Resolve<any> {

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

export const movimientoAppRoute: Routes = [
    {
        path: 'movimiento-app',
        component: MovimientoAppComponent,
        resolve: {
            'pagingParams': MovimientoAppResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'paynowApp.movimientoApp.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'movimiento-app/:id',
        component: MovimientoAppDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'paynowApp.movimientoApp.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const movimientoAppPopupRoute: Routes = [
    {
        path: 'movimiento-app-new',
        component: MovimientoAppPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'paynowApp.movimientoApp.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'movimiento-app/:id/edit',
        component: MovimientoAppPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'paynowApp.movimientoApp.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'movimiento-app/:id/delete',
        component: MovimientoAppDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'paynowApp.movimientoApp.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
