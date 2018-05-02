import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { PaynowSharedModule } from '../../shared';
import {
    MovimientoAppService,
    MovimientoAppPopupService,
    MovimientoAppComponent,
    MovimientoAppDetailComponent,
    MovimientoAppDialogComponent,
    MovimientoAppPopupComponent,
    MovimientoAppDeletePopupComponent,
    MovimientoAppDeleteDialogComponent,
    movimientoAppRoute,
    movimientoAppPopupRoute,
    MovimientoAppResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...movimientoAppRoute,
    ...movimientoAppPopupRoute,
];

@NgModule({
    imports: [
        PaynowSharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        MovimientoAppComponent,
        MovimientoAppDetailComponent,
        MovimientoAppDialogComponent,
        MovimientoAppDeleteDialogComponent,
        MovimientoAppPopupComponent,
        MovimientoAppDeletePopupComponent,
    ],
    entryComponents: [
        MovimientoAppComponent,
        MovimientoAppDialogComponent,
        MovimientoAppPopupComponent,
        MovimientoAppDeleteDialogComponent,
        MovimientoAppDeletePopupComponent,
    ],
    providers: [
        MovimientoAppService,
        MovimientoAppPopupService,
        MovimientoAppResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class PaynowMovimientoAppModule {}
