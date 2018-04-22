import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { PaynowSharedModule } from '../../shared';
import {
    SaldoAppService,
    SaldoAppPopupService,
    SaldoAppComponent,
    SaldoAppDetailComponent,
    SaldoAppDialogComponent,
    SaldoAppPopupComponent,
    SaldoAppDeletePopupComponent,
    SaldoAppDeleteDialogComponent,
    saldoAppRoute,
    saldoAppPopupRoute,
    SaldoAppResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...saldoAppRoute,
    ...saldoAppPopupRoute,
];

@NgModule({
    imports: [
        PaynowSharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        SaldoAppComponent,
        SaldoAppDetailComponent,
        SaldoAppDialogComponent,
        SaldoAppDeleteDialogComponent,
        SaldoAppPopupComponent,
        SaldoAppDeletePopupComponent,
    ],
    entryComponents: [
        SaldoAppComponent,
        SaldoAppDialogComponent,
        SaldoAppPopupComponent,
        SaldoAppDeleteDialogComponent,
        SaldoAppDeletePopupComponent,
    ],
    providers: [
        SaldoAppService,
        SaldoAppPopupService,
        SaldoAppResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class PaynowSaldoAppModule {}
