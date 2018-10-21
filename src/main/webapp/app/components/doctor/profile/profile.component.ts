import { Component, OnInit } from '@angular/core';
import { Doctor, DoctorService, TimeSlot } from 'app/components';
import { ActivatedRoute } from '@angular/router';
import { weekdays } from 'moment';
import moment = require('moment');
import { Principal } from 'app/core';

@Component({
    selector: 'jhi-profile',
    templateUrl: './profile.component.html',
    styles: []
})
export class ProfileComponent implements OnInit {
    doctor: Doctor = new Doctor();
    isSaving: boolean;
    days;
    times: any[] = [];

    constructor(private route: ActivatedRoute, private principal: Principal, private doctorService: DoctorService) {}

    ngOnInit() {
        this.days = weekdays();
        const m: any = moment(new Date(2018, 1, 1, 0, 0, 0));
        const duration = 30;
        for (let i = 0; i < 24 * 60; i += duration) {
            this.times.push(m.format('HH:mm:ss'));
            m.add(duration, 'minutes');
        }
        this.isSaving = false;
        let doctor_id: any;
        if (this.route.snapshot.paramMap.get('doctor_id') != null) {
            doctor_id = this.route.snapshot.paramMap.get('doctor_id');
            this.doctorService.find(doctor_id).subscribe(response => {
                if (response.status === 200) {
                    this.doctor = response.body;
                }
            });
        } else {
            this.principal.identity().then(account => {
                this.doctorService.findByUserId(account.id).subscribe(response => {
                    if (response.status === 200) {
                        this.doctor = response.body;
                    }
                    if (this.doctor.timeslots == null) {
                        this.doctor = new Doctor();
                        this.doctor.userDTO = account;
                        this.doctor.userId = account.id;
                    }
                });
            });
        }
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        this.doctor.timeslots.forEach(ts => {
            delete ts.id;
        });
        console.log(this.doctor);
        if (this.doctor.id !== null) {
            this.doctorService.update(this.doctor).subscribe(response => this.onSaveSuccess(response), () => this.onSaveError());
        } else {
            this.doctorService.create(this.doctor).subscribe(response => this.onSaveSuccess(response), () => this.onSaveError());
        }
    }

    private onSaveSuccess(result) {
        this.isSaving = false;
        this.ngOnInit();
    }

    private onSaveError() {
        this.isSaving = false;
    }

    addTimeSlot() {
        this.doctor.timeslots.push(new TimeSlot());
    }

    removeTimeSlot(timeslot: TimeSlot) {
        const index = this.doctor.timeslots.indexOf(timeslot);
        this.doctor.timeslots.splice(index, 1);
    }
}
