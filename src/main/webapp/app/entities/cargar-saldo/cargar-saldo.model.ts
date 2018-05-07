import { BaseEntity, User } from './../../shared';

export class CargarSaldo implements BaseEntity {
    constructor(
        public id?: number,
        public monto?: number,
        public usuario?: User,
        public cuenta?: BaseEntity,
        public transferenciaApp?: BaseEntity,
    ) {
    }
}
