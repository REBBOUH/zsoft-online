import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Appointment, AppointmentService, Doctor, DoctorService } from 'app/components';
import { Principal } from 'app/core';
import moment = require('moment');
import { UserMinService } from 'app/components/userMin/user.min.service';

@Component({
    selector: 'jhi-take-appointment',
    templateUrl: './take-appointment.component.html',
    styles: []
})
export class TakeAppointmentComponent implements OnInit {
    appointment: Appointment;
    availableAppointments: Appointment[];
    doctors: Doctor[];
    date: any;
    doctor_id: any;
    isSaving: boolean;
    minDate: any = new Date();
    account: any;

    constructor(
        private route: ActivatedRoute,
        private appointmentService: AppointmentService,
        private doctorService: DoctorService,
        private userMinService: UserMinService,
        private principal: Principal
    ) {}

    ngOnInit() {
        this.appointment = null;
        this.availableAppointments = [];
        this.doctors = [];
        this.isSaving = false;
        this.doctorService.query().subscribe(response => {
            if (response.status !== 200) {
                this.doctors = [];
            } else {
                this.doctors = response.body;
                this.doctors = this.doctors.map(doctor => {
                    this.userMinService.find(doctor.userId).subscribe(response2 => {
                        if (response2.status === 200) {
                            doctor.firstName = response2.body.firstName;
                            doctor.lastName = response2.body.lastName;
                            doctor.email = response2.body.email;
                            doctor.imageUrl = response2.body.imageUrl;
                        }
                    });
                    return doctor;
                });
            }
        });

        this.principal.identity().then(account => {
            this.account = account;
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
        this.doctor_id = event.target.value;
        this.getAvailableAppointments();
    }

    setDate(event: any) {
        this.date = event.target.value;
        if (moment(event.target.value).isAfter(this.minDate)) {
            this.getAvailableAppointments();
        } else {
            this.availableAppointments = [];
        }
    }

    getAvailableAppointments() {
        this.availableAppointments = [];
        if (this.doctor_id != null && this.date != null) {
            this.appointmentService.available(this.doctor_id, this.date).subscribe(response => {
                if (response.status !== 200) {
                    this.availableAppointments = [];
                } else {
                    this.availableAppointments = response.body;
                }
            });
        }
    }

    setAppointment(event: any) {
        this.appointment = this.availableAppointments[event.target.value];
        this.appointment.patientId = this.account.id;
    }
}
