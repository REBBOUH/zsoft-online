import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Appointment, AppointmentService } from 'app/components';
import { Principal } from 'app/core';
import moment = require('moment');

@Component({
    selector: 'jhi-update-appointment',
    templateUrl: './update-appointment.component.html',
    styles: []
})
export class UpdateAppointmentComponent implements OnInit {
    appointment: Appointment;
    currentAppointment: Appointment;
    availableAppointments: Appointment[];
    endTimes: any[];
    isInit: boolean;
    isSaving: boolean;
    minDate: any = new Date();

    constructor(private route: ActivatedRoute, private appointmentService: AppointmentService, private principal: Principal) {}

    ngOnInit() {
        this.availableAppointments = [];
        this.endTimes = [];
        this.isSaving = false;
        this.isInit = true;
        const appointment_id: any = this.route.snapshot.paramMap.get('appointment_id');
        this.appointmentService.find(appointment_id).subscribe(response => {
            if (response.status !== 200) {
                this.appointment = null;
                this.currentAppointment = null;
            } else {
                this.appointment = response.body;
                if (moment(this.appointment.date).isBefore(this.minDate)) {
                    this.minDate = moment(this.appointment.date).toDate();
                }
                this.currentAppointment = new Appointment(
                    null,
                    null,
                    null,
                    response.body.date,
                    response.body.timeStart,
                    response.body.timeEnd,
                    null,
                    null,
                    null
                );
            }
            this.getAvailableAppointments();
        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.appointment !== null) {
            this.appointmentService.update(this.appointment).subscribe(response => this.onSaveSuccess(response), () => this.onSaveError());
        }
    }

    private onSaveSuccess(result) {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }

    setDate(event: any) {
        this.appointment.date = event.target.value;
        if (moment(event.target.value).isAfter(this.minDate)) {
            this.getAvailableAppointments();
        } else {
            this.availableAppointments = [];
        }
    }

    getAvailableAppointments() {
        this.availableAppointments = [];
        this.endTimes = [];
        this.appointment.timeStart = null;
        this.appointment.timeEnd = null;
        if (this.appointment.doctor.id != null && this.appointment.date != null) {
            this.appointmentService.available(this.appointment.doctor.id, this.appointment.date).subscribe(response => {
                if (response.status !== 200) {
                    this.availableAppointments = [];
                } else {
                    this.availableAppointments = response.body;
                    if (this.appointment.date === this.currentAppointment.date) {
                        const cts = moment(this.currentAppointment.timeStart, 'HH:mm:ss');
                        const cte = moment(this.currentAppointment.timeEnd, 'HH:mm:ss');
                        this.availableAppointments.forEach(ap => {
                            const ts = moment(ap.timeStart, 'HH:mm:ss');
                            const te = moment(ap.timeEnd, 'HH:mm:ss');
                            if (ts === cts || (ts.isAfter(cts) && ts.isBefore(cte))) {
                                ap.status = 'Available';
                                return;
                            }
                            if (te === cte || (te.isAfter(cts) && te.isBefore(cte))) {
                                ap.status = 'Available';
                                return;
                            }
                        });
                    }
                    if (this.isInit) {
                        this.setStartTime();
                    }
                }
            });
        }
    }

    setStartTime(event?: any) {
        let timeStart;
        if (event !== undefined) {
            timeStart = event.target.value;
        } else if (this.isInit) {
            this.appointment.timeStart = this.currentAppointment.timeStart;
            timeStart = this.currentAppointment.timeStart;
        } else {
            return;
        }
        this.endTimes = [];
        let append = false;
        this.availableAppointments.forEach(ap => {
            if (ap.timeStart === timeStart) {
                append = true;
            }
            if (append && ap.status === 'Occuped') {
                append = false;
                return;
            }
            if (append) {
                this.endTimes.push(ap.timeEnd);
            }
        });
        if (this.isInit) {
            this.appointment.timeEnd = this.currentAppointment.timeEnd;
            this.isInit = false;
        }
    }
}
