import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { PaynowAppModule } from './app/app.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    imports: [
        PaynowAppModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class PaynowEntityModule {}
