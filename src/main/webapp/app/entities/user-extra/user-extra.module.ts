import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { PaynowSharedModule } from '../../shared';
import { PaynowAdminModule } from '../../admin/admin.module';
import {
    UserExtraService,
    UserExtraPopupService,
    UserExtraComponent,
    UserExtraDetailComponent,
    UserExtraDialogComponent,
    UserExtraPopupComponent,
    UserExtraDeletePopupComponent,
    UserExtraDeleteDialogComponent,
    userExtraRoute,
    userExtraPopupRoute,
    UserExtraResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...userExtraRoute,
    ...userExtraPopupRoute,
];

@NgModule({
    imports: [
        PaynowSharedModule,
        PaynowAdminModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        UserExtraComponent,
        UserExtraDetailComponent,
        UserExtraDialogComponent,
        UserExtraDeleteDialogComponent,
        UserExtraPopupComponent,
        UserExtraDeletePopupComponent,
    ],
    entryComponents: [
        UserExtraComponent,
        UserExtraDialogComponent,
        UserExtraPopupComponent,
        UserExtraDeleteDialogComponent,
        UserExtraDeletePopupComponent,
    ],
    providers: [
        UserExtraService,
        UserExtraPopupService,
        UserExtraResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class PaynowUserExtraModule {}
