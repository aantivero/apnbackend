import { BaseEntity } from './../../shared';

export const enum Moneda {
    'PESOS',
    ' DOLAR'
}

export class MovimientoApp implements BaseEntity {
    constructor(
        public id?: number,
        public cuentaDebitoCbu?: string,
        public cuentaDebitoAlias?: string,
        public cuentaDebitoPropia?: boolean,
        public cuentaCreditoCbu?: string,
        public cuentaCreditoAlias?: string,
        public cuentaCreditoPropia?: boolean,
        public moneda?: Moneda,
        public monto?: number,
        public timestamp?: any,
        public descripcion?: string,
        public identificacion?: string,
        public consolidado?: boolean,
        public consolidadoTimestamp?: any,
        public bancoDebito?: BaseEntity,
        public bancoCredito?: BaseEntity,
    ) {
        this.cuentaDebitoPropia = false;
        this.cuentaCreditoPropia = false;
        this.consolidado = false;
    }
}
