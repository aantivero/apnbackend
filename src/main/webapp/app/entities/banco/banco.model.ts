import { BaseEntity } from './../../shared';

export class Banco implements BaseEntity {
    constructor(
        public id?: number,
        public nombre?: string,
        public codigo?: string,
    ) {
    }
}
