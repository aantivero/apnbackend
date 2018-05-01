import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { PaynowSharedModule } from '../../shared';
import {
    TransferenciaAppService,
    TransferenciaAppPopupService,
    TransferenciaAppComponent,
    TransferenciaAppDetailComponent,
    TransferenciaAppDialogComponent,
    TransferenciaAppPopupComponent,
    TransferenciaAppDeletePopupComponent,
    TransferenciaAppDeleteDialogComponent,
    transferenciaAppRoute,
    transferenciaAppPopupRoute,
    TransferenciaAppResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...transferenciaAppRoute,
    ...transferenciaAppPopupRoute,
];

@NgModule({
    imports: [
        PaynowSharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        TransferenciaAppComponent,
        TransferenciaAppDetailComponent,
        TransferenciaAppDialogComponent,
        TransferenciaAppDeleteDialogComponent,
        TransferenciaAppPopupComponent,
        TransferenciaAppDeletePopupComponent,
    ],
    entryComponents: [
        TransferenciaAppComponent,
        TransferenciaAppDialogComponent,
        TransferenciaAppPopupComponent,
        TransferenciaAppDeleteDialogComponent,
        TransferenciaAppDeletePopupComponent,
    ],
    providers: [
        TransferenciaAppService,
        TransferenciaAppPopupService,
        TransferenciaAppResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class PaynowTransferenciaAppModule {}
