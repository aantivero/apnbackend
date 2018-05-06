import { BaseEntity, User } from './../../shared';

export const enum TipoDocumento {
    'DNI',
    ' RUC',
    ' PASPORT'
}

export const enum TipoContribuyente {
    'RN',
    ' RNI',
    ' EX',
    ' CF',
    ' RM',
    ' NC',
    ' NR'
}

export class UserExtra implements BaseEntity {
    constructor(
        public id?: number,
        public telefono?: string,
        public vendedor?: boolean,
        public tipoDocumento?: TipoDocumento,
        public numeroDocumento?: string,
        public tipoContribuyente?: TipoContribuyente,
        public usuario?: User,
    ) {
        this.vendedor = false;
    }
}
