import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { PaynowSharedModule } from '../../shared';
import { PaynowAdminModule } from '../../admin/admin.module';
import {
    SaldoService,
    SaldoPopupService,
    SaldoComponent,
    SaldoDetailComponent,
    SaldoDialogComponent,
    SaldoPopupComponent,
    SaldoDeletePopupComponent,
    SaldoDeleteDialogComponent,
    saldoRoute,
    saldoPopupRoute,
    SaldoResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...saldoRoute,
    ...saldoPopupRoute,
];

@NgModule({
    imports: [
        PaynowSharedModule,
        PaynowAdminModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        SaldoComponent,
        SaldoDetailComponent,
        SaldoDialogComponent,
        SaldoDeleteDialogComponent,
        SaldoPopupComponent,
        SaldoDeletePopupComponent,
    ],
    entryComponents: [
        SaldoComponent,
        SaldoDialogComponent,
        SaldoPopupComponent,
        SaldoDeleteDialogComponent,
        SaldoDeletePopupComponent,
    ],
    providers: [
        SaldoService,
        SaldoPopupService,
        SaldoResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class PaynowSaldoModule {}
