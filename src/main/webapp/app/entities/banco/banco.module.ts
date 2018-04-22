import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { PaynowSharedModule } from '../../shared';
import {
    BancoService,
    BancoPopupService,
    BancoComponent,
    BancoDetailComponent,
    BancoDialogComponent,
    BancoPopupComponent,
    BancoDeletePopupComponent,
    BancoDeleteDialogComponent,
    bancoRoute,
    bancoPopupRoute,
    BancoResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...bancoRoute,
    ...bancoPopupRoute,
];

@NgModule({
    imports: [
        PaynowSharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        BancoComponent,
        BancoDetailComponent,
        BancoDialogComponent,
        BancoDeleteDialogComponent,
        BancoPopupComponent,
        BancoDeletePopupComponent,
    ],
    entryComponents: [
        BancoComponent,
        BancoDialogComponent,
        BancoPopupComponent,
        BancoDeleteDialogComponent,
        BancoDeletePopupComponent,
    ],
    providers: [
        BancoService,
        BancoPopupService,
        BancoResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class PaynowBancoModule {}
