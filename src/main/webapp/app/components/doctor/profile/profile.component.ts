import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { weekdays } from 'moment';
import { Principal } from 'app/core';
import { Configuration, Doctor, DoctorService, TimeSlot } from 'app/components';
import moment = require('moment');

@Component({
    selector: 'jhi-profile',
    templateUrl: './profile.component.html',
    styles: []
})
export class ProfileComponent implements OnInit {
    doctor: Doctor = new Doctor();
    configurations: Configuration[] = [];
    defaultConfiguration: string;
    timeSlots: TimeSlot[] = [];
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
            this.doctorService.find(doctor_id).subscribe(response => this.doctorIsFetshed(response));
        } else {
            this.principal.identity().then(account => {
                this.doctorService.findByUserId(account.id).subscribe(response => this.doctorIsFetshed(response));
                if (this.doctor == null) {
                    this.doctor = new Doctor();
                }
                this.doctor.userId = account.id;
            });
        }
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.timeslotsToConf();
        if (this.defaultConfiguration != null && this.defaultConfiguration !== JSON.stringify(this.configurations)) {
            if (!confirm('Are you sure you want to update the configurations ?')) {
                return false;
            }
        }
        this.isSaving = true;
        if (this.doctor.id !== null) {
            this.doctorService.update(this.doctor).subscribe(response => this.onSaveSuccess(response), () => this.onSaveError());
        } else {
            this.doctorService.create(this.doctor).subscribe(response => this.onSaveSuccess(response), () => this.onSaveError());
        }
    }

    private onSaveSuccess(result) {
        this.isSaving = false;
        if (this.defaultConfiguration != null && this.defaultConfiguration !== JSON.stringify(this.configurations)) {
            this.doctorService.updateConfigurations(result.body.id, this.configurations).subscribe(response => {
                this.ngOnInit();
            });
        } else {
            this.ngOnInit();
        }
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private doctorIsFetshed(response) {
        if (response.status === 200) {
            this.doctor = response.body;
            if (this.doctor.id != null) {
                this.doctorService.getConfigurations(this.doctor.id).subscribe(res => {
                    this.configurations = res.body;
                    this.confToTimeslots();
                    this.timeslotsToConf();
                    this.defaultConfiguration = JSON.stringify(this.configurations);
                });
            }
        }
    }

    addTimeSlot() {
        const timeslot: TimeSlot = new TimeSlot();
        timeslot.id = Math.floor((1 + Math.random()) * 10000000).toString();
        this.timeSlots.push(timeslot);
    }

    removeTimeSlot(timeslot: TimeSlot) {
        const index = this.timeSlots.indexOf(timeslot);
        this.timeSlots.splice(index, 1);
    }

    private timeslotsToConf() {
        this.configurations = [];
        this.timeSlots.forEach(timeslot => {
            this.configurations.push(new Configuration(null, 'DOCTOR', this.doctor.id, '@TIME_SLOT', timeslot.id));
            this.configurations.push(new Configuration(null, '@TIME_SLOT', timeslot.id, 'DAY_OF_WEEK', timeslot.dayOfWeek.toString()));
            this.configurations.push(new Configuration(null, '@TIME_SLOT', timeslot.id, 'TIME_START', timeslot.timeStart.toString()));
            this.configurations.push(new Configuration(null, '@TIME_SLOT', timeslot.id, 'TIME_END', timeslot.timeEnd.toString()));
            return timeslot;
        });
    }

    private confToTimeslots() {
        this.timeSlots = [];
        this.configurations.filter(conf => conf.entity === 'DOCTOR').forEach(conf => {
            const timeslot: TimeSlot = new TimeSlot();
            timeslot.id = conf.value;
            this.configurations
                .filter(ts => ts.entity === conf.key && ts.entityId.toString() === conf.value && ts.key === 'DAY_OF_WEEK')
                .map(ts => {
                    timeslot.dayOfWeek = parseInt(ts.value, 10);
                });
            this.configurations
                .filter(ts => ts.entity === conf.key && ts.entityId.toString() === conf.value && ts.key === 'TIME_START')
                .map(ts => {
                    timeslot.timeStart = ts.value;
                });
            this.configurations
                .filter(ts => ts.entity === conf.key && ts.entityId.toString() === conf.value && ts.key === 'TIME_END')
                .map(ts => {
                    timeslot.timeEnd = ts.value;
                });
            this.timeSlots.push(timeslot);
        });
    }
}
