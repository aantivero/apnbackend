import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { PaynowAppModule } from './app/app.module';
import { PaynowSaldoAppModule } from './saldo-app/saldo-app.module';
import { PaynowBancoModule } from './banco/banco.module';
import { PaynowCuentaAppModule } from './cuenta-app/cuenta-app.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    imports: [
        PaynowAppModule,
        PaynowSaldoAppModule,
        PaynowBancoModule,
        PaynowCuentaAppModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class PaynowEntityModule {}
