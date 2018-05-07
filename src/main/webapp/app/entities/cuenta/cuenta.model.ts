import { BaseEntity, User } from './../../shared';

export const enum Moneda {
    'PESOS',
    ' DOLAR'
}

export const enum TipoCuenta {
    'VIRTUAL',
    ' BANCARIA'
}

export class Cuenta implements BaseEntity {
    constructor(
        public id?: number,
        public nombre?: string,
        public descripcion?: string,
        public moneda?: Moneda,
        public saldo?: number,
        public tipo?: TipoCuenta,
        public cbu?: string,
        public aliasCbu?: string,
        public fechaCreacion?: any,
        public fechaModificacion?: any,
        public habilitada?: boolean,
        public paraCredito?: boolean,
        public paraDebito?: boolean,
        public maximoCredito?: number,
        public maximoDebito?: number,
        public codigoSeguridad?: string,
        public usuario?: User,
        public banco?: BaseEntity,
    ) {
        this.habilitada = false;
        this.paraCredito = false;
        this.paraDebito = false;
    }
}
