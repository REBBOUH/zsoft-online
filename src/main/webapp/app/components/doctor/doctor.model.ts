import { TimeSlot } from 'app/components';

export interface IDoctor {
    id?: any;
    userId?: any;
    userDTO?: any;
    phone?: string;
    address?: string;
    gender?: string;
    speciality?: string;
    timeslots?: TimeSlot[];
}

export class Doctor implements IDoctor {
    constructor(
        public id?: any,
        public userDTO?: any,
        public userId?: any,
        public phone?: string,
        public address?: string,
        public gender?: string,
        public speciality?: string,
        public timeslots?: TimeSlot[]
    ) {
        this.id = id ? id : null;
        this.userDTO = userDTO ? userDTO : null;
        this.userId = userId ? userId : null;
        this.phone = phone ? phone : null;
        this.address = address ? address : null;
        this.gender = gender ? gender : null;
        this.speciality = speciality ? speciality : null;
        this.timeslots = timeslots ? timeslots : [];
    }
}
