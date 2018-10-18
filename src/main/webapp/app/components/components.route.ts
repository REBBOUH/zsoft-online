import { Routes } from '@angular/router';
import { DoctorComponent } from './doctor/doctor.component';
import { ProfileComponent } from './doctor/profile/profile.component';
import { AppointmentComponent } from './doctor/appointment/appointment.component';

export const componentsState: Routes = [
    {
        path: '',
        children: [
            {
                path: 'doctor',
                data: {
                    pageTitle: 'doctorManagement.home.title'
                },
                component: DoctorComponent
            },
            {
                path: 'doctor/:doctor_id/appointment',
                data: {
                    pageTitle: 'doctorManagement.home.title'
                },
                component: AppointmentComponent
            },
            {
                path: 'doctor/:doctor_id/profile',
                data: {
                    pageTitle: 'doctorManagement.home.title'
                },
                component: ProfileComponent
            }
        ]
    }
];
