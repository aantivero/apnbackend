import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { PaynowSharedModule } from '../../shared';
import { PaynowAdminModule } from '../../admin/admin.module';
import {
    CuentaService,
    CuentaPopupService,
    CuentaComponent,
    CuentaDetailComponent,
    CuentaDialogComponent,
    CuentaPopupComponent,
    CuentaDeletePopupComponent,
    CuentaDeleteDialogComponent,
    cuentaRoute,
    cuentaPopupRoute,
    CuentaResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...cuentaRoute,
    ...cuentaPopupRoute,
];

@NgModule({
    imports: [
        PaynowSharedModule,
        PaynowAdminModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        CuentaComponent,
        CuentaDetailComponent,
        CuentaDialogComponent,
        CuentaDeleteDialogComponent,
        CuentaPopupComponent,
        CuentaDeletePopupComponent,
    ],
    entryComponents: [
        CuentaComponent,
        CuentaDialogComponent,
        CuentaPopupComponent,
        CuentaDeleteDialogComponent,
        CuentaDeletePopupComponent,
    ],
    providers: [
        CuentaService,
        CuentaPopupService,
        CuentaResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class PaynowCuentaModule {}
