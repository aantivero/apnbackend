import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { PaynowSharedModule } from '../../shared';
import {
    CuentaAppService,
    CuentaAppPopupService,
    CuentaAppComponent,
    CuentaAppDetailComponent,
    CuentaAppDialogComponent,
    CuentaAppPopupComponent,
    CuentaAppDeletePopupComponent,
    CuentaAppDeleteDialogComponent,
    cuentaAppRoute,
    cuentaAppPopupRoute,
    CuentaAppResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...cuentaAppRoute,
    ...cuentaAppPopupRoute,
];

@NgModule({
    imports: [
        PaynowSharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        CuentaAppComponent,
        CuentaAppDetailComponent,
        CuentaAppDialogComponent,
        CuentaAppDeleteDialogComponent,
        CuentaAppPopupComponent,
        CuentaAppDeletePopupComponent,
    ],
    entryComponents: [
        CuentaAppComponent,
        CuentaAppDialogComponent,
        CuentaAppPopupComponent,
        CuentaAppDeleteDialogComponent,
        CuentaAppDeletePopupComponent,
    ],
    providers: [
        CuentaAppService,
        CuentaAppPopupService,
        CuentaAppResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class PaynowCuentaAppModule {}
