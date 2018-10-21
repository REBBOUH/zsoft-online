export interface ITimeSlot {
    id?: any;
    dayOfWeek?: number;
    timeStart?: string;
    timeEnd?: string;
    status?: boolean;
}

export class TimeSlot implements ITimeSlot {
    constructor(public id?: any, public dayOfWeek?: number, public timeStart?: string, public timeEnd?: string, public status?: boolean) {
        this.id = id ? id : null;
        this.dayOfWeek = dayOfWeek ? dayOfWeek : 0;
        this.timeStart = timeStart ? timeStart : '08:00:00';
        this.timeEnd = timeEnd ? timeEnd : '12:00:00';
        this.status = status ? status : true;
    }
}
