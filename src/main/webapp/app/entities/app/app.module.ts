import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { PaynowSharedModule } from '../../shared';
import {
    AppService,
    AppPopupService,
    AppComponent,
    AppDetailComponent,
    AppDialogComponent,
    AppPopupComponent,
    AppDeletePopupComponent,
    AppDeleteDialogComponent,
    appRoute,
    appPopupRoute,
    AppResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...appRoute,
    ...appPopupRoute,
];

@NgModule({
    imports: [
        PaynowSharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        AppComponent,
        AppDetailComponent,
        AppDialogComponent,
        AppDeleteDialogComponent,
        AppPopupComponent,
        AppDeletePopupComponent,
    ],
    entryComponents: [
        AppComponent,
        AppDialogComponent,
        AppPopupComponent,
        AppDeleteDialogComponent,
        AppDeletePopupComponent,
    ],
    providers: [
        AppService,
        AppPopupService,
        AppResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class PaynowAppModule {}
