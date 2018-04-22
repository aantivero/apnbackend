import { BaseEntity } from './../../shared';

export class App implements BaseEntity {
    constructor(
        public id?: number,
        public nombre?: string,
    ) {
    }
}
