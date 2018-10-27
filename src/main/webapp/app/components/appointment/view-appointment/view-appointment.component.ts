import { Component, OnInit } from '@angular/core';
import { Appointment, AppointmentService, DoctorService } from 'app/components';
import { ActivatedRoute } from '@angular/router';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { UserMinService } from 'app/components/userMin/user.min.service';

@Component({
    selector: 'jhi-view-appointment',
    templateUrl: './view-appointment.component.html',
    styles: []
})
export class ViewAppointmentComponent implements OnInit {
    appointment: Appointment;

    constructor(
        private route: ActivatedRoute,
        private doctorService: DoctorService,
        private userMinService: UserMinService,
        public activeModal: NgbActiveModal
    ) {}

    ngOnInit() {
        this.userMinService.find(this.appointment.patientId).subscribe(response1 => {
            if (response1.status === 200) {
                this.appointment.patientName = response1.body.lastName.toUpperCase() + ' ' + response1.body.firstName;
            }
        });
        this.doctorService.find(this.appointment.doctorId).subscribe(response1 => {
            if (response1.status === 200) {
                const doctor = response1.body;
                this.userMinService.find(doctor.userId).subscribe(response2 => {
                    if (response2.status === 200) {
                        this.appointment.doctorName = response2.body.lastName.toUpperCase() + ' ' + response2.body.firstName;
                    }
                });
            }
        });
    }

    clear() {
        // this.activeModal.dismiss('cancel');
        this.activeModal.close();
    }
}
