export interface IUserMin {
    id?: any;
    firstName?: string;
    lastName?: string;
    email?: string;
    imageUrl?: string;
}

export class UserMin implements IUserMin {
    constructor(public id?: any, public firstName?: string, public lastName?: string, public email?: string, public imageUrl?: string) {
        this.id = id ? id : null;
        this.firstName = firstName ? firstName : '';
        this.lastName = lastName ? lastName : '';
        this.email = email ? email : '';
        this.imageUrl = imageUrl ? imageUrl : '';
    }
}
