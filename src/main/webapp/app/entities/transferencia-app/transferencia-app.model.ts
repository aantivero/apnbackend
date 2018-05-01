import { BaseEntity } from './../../shared';

export const enum Moneda {
    'PESOS',
    ' DOLAR'
}

export const enum EstadoTransferencia {
    'ENVIADA',
    ' ACEPTADA',
    ' RECHAZADA',
    ' ERROR'
}

export class TransferenciaApp implements BaseEntity {
    constructor(
        public id?: number,
        public destinoCbu?: string,
        public destinoAlias?: string,
        public destinoInfo?: string,
        public moneda?: Moneda,
        public monto?: number,
        public descripcion?: string,
        public estadoTransferencia?: EstadoTransferencia,
        public timestamp?: any,
        public descripcionEstado?: string,
        public identificacion?: string,
        public origen?: BaseEntity,
        public destinoBanco?: BaseEntity,
    ) {
    }
}
