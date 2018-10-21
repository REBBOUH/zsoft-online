import { Component, OnInit } from '@angular/core';
import { Appointment, AppointmentService } from 'app/components';
import { ActivatedRoute } from '@angular/router';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

@Component({
    selector: 'jhi-view-appointment',
    templateUrl: './view-appointment.component.html',
    styles: []
})
export class ViewAppointmentComponent implements OnInit {
    appointment: Appointment;

    constructor(private route: ActivatedRoute, public activeModal: NgbActiveModal, private appointmentService: AppointmentService) {}

    ngOnInit() {
        const appointment_id: any = this.route.snapshot.paramMap.get('appointment_id');
        this.appointmentService.find(appointment_id).subscribe(response => {
            if (response.status !== 200) {
                this.appointment = null;
            } else {
                this.appointment = response.body;
            }
        });
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }
}
