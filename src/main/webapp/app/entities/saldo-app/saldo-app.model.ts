import { BaseEntity } from './../../shared';

export const enum Moneda {
    'PESOS',
    ' DOLAR'
}

export class SaldoApp implements BaseEntity {
    constructor(
        public id?: number,
        public moneda?: Moneda,
        public monto?: number,
        public app?: BaseEntity,
    ) {
    }
}
