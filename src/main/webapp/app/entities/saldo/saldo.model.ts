import { BaseEntity, User } from './../../shared';

export const enum Moneda {
    'PESOS',
    ' DOLAR'
}

export const enum TipoCuenta {
    'VIRTUAL',
    ' BANCARIA'
}

export class Saldo implements BaseEntity {
    constructor(
        public id?: number,
        public monto?: number,
        public moneda?: Moneda,
        public tipo?: TipoCuenta,
        public fechaCreacion?: any,
        public fechaModificacion?: any,
        public usuario?: User,
        public aplicacion?: BaseEntity,
    ) {
    }
}
