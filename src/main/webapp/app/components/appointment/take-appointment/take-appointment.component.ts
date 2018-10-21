import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Appointment, AppointmentService, Doctor, DoctorService } from 'app/components';
import { Principal } from 'app/core';
import moment = require('moment');

@Component({
    selector: 'jhi-take-appointment',
    templateUrl: './take-appointment.component.html',
    styles: []
})
export class TakeAppointmentComponent implements OnInit {
    appointment: Appointment;
    availableAppointments: Appointment[];
    doctors: Doctor[];
    endTimes: any[];
    isSaving: boolean;
    minDate: any = new Date();

    constructor(
        private route: ActivatedRoute,
        private appointmentService: AppointmentService,
        private doctorService: DoctorService,
        private principal: Principal
    ) {}

    ngOnInit() {
        this.appointment = new Appointment();
        this.appointment.status = 'Panding';
        this.availableAppointments = [];
        this.endTimes = [];
        this.doctors = [];
        this.isSaving = false;
        this.doctorService.query().subscribe(response => {
            if (response.status !== 200) {
                this.doctors = [];
            } else {
                this.doctors = response.body;
            }
        });

        this.principal.identity().then(account => {
            this.appointment.patient = { id: account.id };
        });
        this.getAvailableAppointments();
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.appointment !== null) {
            this.appointmentService.take(this.appointment).subscribe(response => this.onSaveSuccess(response), () => this.onSaveError());
        }
    }

    private onSaveSuccess(result) {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }

    setDoctor(event: any) {
        this.appointment.doctor = { id: event.target.value };
        this.getAvailableAppointments();
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
        if (
            this.appointment != null &&
            this.appointment.doctor != null &&
            this.appointment.doctor.id != null &&
            this.appointment.date != null
        ) {
            this.appointmentService.available(this.appointment.doctor.id, this.appointment.date).subscribe(response => {
                if (response.status !== 200) {
                    this.availableAppointments = [];
                } else {
                    this.availableAppointments = response.body;
                }
            });
        }
    }

    setStartTime(event: any) {
        const timeStart = event.target.value;
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
    }
}
