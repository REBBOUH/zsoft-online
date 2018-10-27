export interface IConfiguration {
    id?: any;
    entity?: string;
    entityId?: any;
    key?: string;
    value?: string;
}

export class Configuration implements IConfiguration {
    constructor(public id?: any, public entity?: string, public entityId?: any, public key?: string, public value?: string) {
        this.id = id ? id : null;
        this.entity = entity ? entity : null;
        this.entityId = entityId ? entityId : null;
        this.key = key ? key : null;
        this.value = value ? value : null;
    }
}
