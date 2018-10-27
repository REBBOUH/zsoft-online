import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { ZonlineSharedModule } from 'app/shared';
import { DatePipe, UpperCasePipe } from '@angular/common';
import { ProfileComponent } from 'app/components/doctor/profile/profile.component';
import { DoctorComponent } from 'app/components/doctor/doctor.component';
import { TakeAppointmentComponent } from 'app/components/appointment/take-appointment/take-appointment.component';
import { AppointmentComponent } from 'app/components/appointment/appointment.component';
import { ViewAppointmentComponent } from 'app/components/appointment/view-appointment/view-appointment.component';
import { CancelAppointmentComponent } from 'app/components/appointment/cancel-appointment/cancel-appointment.component';
import { componentsState } from 'app/components/components.route';

@NgModule({
    imports: [ZonlineSharedModule, RouterModule.forChild(componentsState)],
    declarations: [
        DoctorComponent,
        ProfileComponent,
        AppointmentComponent,
        TakeAppointmentComponent,
        ViewAppointmentComponent,
        CancelAppointmentComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA],
    providers: [UpperCasePipe, DatePipe],
    entryComponents: [CancelAppointmentComponent]
})
export class ComponentsModule {}
