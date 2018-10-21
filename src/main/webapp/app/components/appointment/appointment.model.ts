export interface IAppointment {
    id?: any;
    doctorId?: any;
    patientId?: any;
    date?: Date;
    timeStart?: string;
    timeEnd?: string;
    status?: string;
    doctor?: any;
    patient?: any;
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
        public doctor?: any,
        public patient?: any
    ) {
        this.id = id ? id : null;
        this.doctorId = doctorId ? doctorId : null;
        this.patientId = patientId ? patientId : null;
        this.date = date ? date : null;
        this.timeStart = timeStart ? timeStart : null;
        this.timeEnd = timeEnd ? timeEnd : null;
        this.status = status ? status : 'Panding';
        this.doctor = doctor ? doctor : null;
        this.patient = patient ? patient : null;
    }
}
