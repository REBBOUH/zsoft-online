import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { ZonlineSharedModule } from 'app/shared';

import { AppointmentComponent, componentsState, DoctorComponent, ProfileComponent } from './';

@NgModule({
    imports: [ZonlineSharedModule, RouterModule.forChild(componentsState)],
    declarations: [DoctorComponent, ProfileComponent, AppointmentComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ComponentsModule {}
