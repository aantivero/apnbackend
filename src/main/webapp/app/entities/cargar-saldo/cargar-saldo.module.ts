import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { PaynowSharedModule } from '../../shared';
import { PaynowAdminModule } from '../../admin/admin.module';
import {
    CargarSaldoService,
    CargarSaldoPopupService,
    CargarSaldoComponent,
    CargarSaldoDetailComponent,
    CargarSaldoDialogComponent,
    CargarSaldoPopupComponent,
    CargarSaldoDeletePopupComponent,
    CargarSaldoDeleteDialogComponent,
    cargarSaldoRoute,
    cargarSaldoPopupRoute,
    CargarSaldoResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...cargarSaldoRoute,
    ...cargarSaldoPopupRoute,
];

@NgModule({
    imports: [
        PaynowSharedModule,
        PaynowAdminModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        CargarSaldoComponent,
        CargarSaldoDetailComponent,
        CargarSaldoDialogComponent,
        CargarSaldoDeleteDialogComponent,
        CargarSaldoPopupComponent,
        CargarSaldoDeletePopupComponent,
    ],
    entryComponents: [
        CargarSaldoComponent,
        CargarSaldoDialogComponent,
        CargarSaldoPopupComponent,
        CargarSaldoDeleteDialogComponent,
        CargarSaldoDeletePopupComponent,
    ],
    providers: [
        CargarSaldoService,
        CargarSaldoPopupService,
        CargarSaldoResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class PaynowCargarSaldoModule {}
