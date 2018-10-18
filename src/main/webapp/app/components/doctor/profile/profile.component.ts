import { Component, OnInit } from '@angular/core';
import { Doctor, DoctorService, IDoctor, ITimeSlot, TimeSlot } from 'app/components';
import { ActivatedRoute } from '@angular/router';
import { UserService } from 'app/core';

@Component({
    selector: 'jhi-profile',
    templateUrl: './profile.component.html',
    styles: []
})
export class ProfileComponent implements OnInit {
    doctor: Doctor;
    timeSlots: TimeSlot[];
    isSaving: boolean;

    constructor(private route: ActivatedRoute, private doctorService: DoctorService) {}

    ngOnInit() {
        this.isSaving = false;
        const doctor_id: any = this.route.snapshot.paramMap.get('doctor_id');
        if (doctor_id != null) {
            this.doctorService.find(doctor_id).subscribe(response => {
                if (response.status != 200) this.doctor = new Doctor();
                this.doctor = response.body;
                this.timeSlots = this.doctor.timeslots;

                console.log('#################### RESPONSE ###################');
                console.log(response);
                console.log(this.doctor);
                console.log(this.timeSlots);
                console.log('#######################################');
            });
        }
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.doctor.id !== null) {
            this.doctorService.update(this.doctor).subscribe(response => this.onSaveSuccess(response), () => this.onSaveError());
        } else {
            this.doctorService.create(this.doctor).subscribe(response => this.onSaveSuccess(response), () => this.onSaveError());
        }
    }

    private onSaveSuccess(result) {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }
}
