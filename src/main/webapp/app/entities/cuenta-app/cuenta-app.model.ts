import { BaseEntity } from './../../shared';

export const enum Moneda {
    'PESOS',
    ' DOLAR'
}

export class CuentaApp implements BaseEntity {
    constructor(
        public id?: number,
        public nombre?: string,
        public aliasCbu?: string,
        public cbu?: string,
        public moneda?: Moneda,
        public saldo?: number,
        public banco?: BaseEntity,
        public app?: BaseEntity,
    ) {
    }
}
