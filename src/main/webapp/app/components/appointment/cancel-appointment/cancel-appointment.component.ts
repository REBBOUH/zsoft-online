import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';
import { Appointment, AppointmentService } from 'app/components';

@Component({
    selector: 'jhi-cancel-appointment',
    templateUrl: './cancel-appointment.component.html',
    styles: []
})
export class CancelAppointmentComponent {
    appointment: Appointment;

    constructor(
        private appointmentService: AppointmentService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmCancel(id) {
        this.appointmentService.cancel(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'AppointmentListModification',
                content: 'Canceled an appointment'
            });
            this.activeModal.dismiss(true);
        });
    }
}
