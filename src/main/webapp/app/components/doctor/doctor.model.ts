export interface IDoctor {
    id?: any;
    userId?: any;
    phone?: string;
    address?: string;
    gender?: string;
    speciality?: string;
    firstName?: string;
    lastName?: string;
    email?: string;
    imageUrl?: string;
}

export class Doctor implements IDoctor {
    constructor(
        public id?: any,
        public userId?: any,
        public phone?: string,
        public address?: string,
        public gender?: string,
        public speciality?: string,
        public firstName?: string,
        public lastName?: string,
        public email?: string,
        public imageUrl?: string
    ) {
        this.id = id ? id : null;
        this.userId = userId ? userId : null;
        this.phone = phone ? phone : null;
        this.address = address ? address : null;
        this.gender = gender ? gender : null;
        this.speciality = speciality ? speciality : null;
        this.firstName = firstName ? firstName : '';
        this.lastName = lastName ? lastName : '';
        this.email = email ? email : '';
        this.imageUrl = imageUrl ? imageUrl : '';
    }
}
