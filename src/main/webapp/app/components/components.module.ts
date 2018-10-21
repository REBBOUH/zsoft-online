import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { ZonlineSharedModule } from 'app/shared';
import {
    AppointmentComponent,
    CancelAppointmentComponent,
    componentsState,
    DoctorComponent,
    ProfileComponent,
    TakeAppointmentComponent,
    UpdateAppointmentComponent
} from './';
import { DatePipe, UpperCasePipe } from '@angular/common';
import { ViewAppointmentComponent } from './appointment/view-appointment/view-appointment.component';

@NgModule({
    imports: [ZonlineSharedModule, RouterModule.forChild(componentsState)],
    declarations: [
        DoctorComponent,
        ProfileComponent,
        AppointmentComponent,
        TakeAppointmentComponent,
        UpdateAppointmentComponent,
        CancelAppointmentComponent,
        ViewAppointmentComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA],
    providers: [UpperCasePipe, DatePipe],
    entryComponents: [CancelAppointmentComponent]
})
export class ComponentsModule {}
