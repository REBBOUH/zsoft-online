export interface IAppointment {
    id?: any;
    doctorId?: any;
    patientId?: any;
    date?: Date;
    timeStart?: string;
    timeEnd?: string;
    status?: string;
    patientName?: string;
    doctorName?: string;
}

export class Appointment implements IAppointment {
    constructor(
        public id?: any,
        public doctorId?: any,
        public patientId?: any,
        public date?: Date,
        public timeStart?: string,
        public timeEnd?: string,
        public status?: string,
        public patientName?: string,
        public doctorName?: string
    ) {
        this.id = id ? id : null;
        this.doctorId = doctorId ? doctorId : null;
        this.patientId = patientId ? patientId : null;
        this.date = date ? date : null;
        this.timeStart = timeStart ? timeStart : null;
        this.timeEnd = timeEnd ? timeEnd : null;
        this.status = status ? status : 'PENDING';
        this.patientName = patientName ? patientName : '';
        this.doctorName = doctorName ? doctorName : '';
    }
}
