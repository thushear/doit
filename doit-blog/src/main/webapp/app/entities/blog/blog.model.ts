import { BaseEntity } from './../../shared';

export class Blog implements BaseEntity {
    constructor(
        public id?: number,
        public title?: string,
        public desc?: string,
        public publicDate?: any,
        public content?: any,
    ) {
    }
}
