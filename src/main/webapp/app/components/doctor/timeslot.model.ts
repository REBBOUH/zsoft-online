export interface ITimeSlot {
    id?: any;
    DayOfWeek?: number;
    timeStart?: string;
    timeEnd?: string;
    Status?: boolean;
}

export class TimeSlot implements ITimeSlot {
    constructor(public id?: any, public DayOfWeek?: number, public timeStart?: string, public timeEnd?: string, public Status?: boolean) {
        this.id = id ? id : null;
        this.DayOfWeek = DayOfWeek ? DayOfWeek : null;
        this.timeStart = timeStart ? timeStart : null;
        this.timeEnd = timeEnd ? timeEnd : null;
        this.Status = Status ? Status : true;
    }
}
