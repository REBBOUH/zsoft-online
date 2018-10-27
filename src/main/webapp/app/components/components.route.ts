import { Routes } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { ProfileComponent } from 'app/components/doctor/profile/profile.component';
import { DoctorComponent } from 'app/components/doctor/doctor.component';
import { TakeAppointmentComponent } from 'app/components/appointment/take-appointment/take-appointment.component';
import { AppointmentComponent } from 'app/components/appointment/appointment.component';
import { ViewAppointmentComponent } from 'app/components/appointment/view-appointment/view-appointment.component';
import { CancelAppointmentComponent } from 'app/components/appointment/cancel-appointment/cancel-appointment.component';

export const componentsState: Routes = [
    {
        path: 'doctors',
        component: DoctorComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_ADMIN'],
            pageTitle: 'doctor.managment.home.title',
            defaultSort: 'id,asc'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'doctor/my-profile',
        data: {
            authorities: ['ROLE_DOCTOR'],
            pageTitle: 'doctor.managment.myprofile.title'
        },
        component: ProfileComponent,
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'doctor/:doctor_id/profile',
        data: {
            authorities: ['ROLE_ADMIN'],
            pageTitle: 'doctor.managment.doctorprofile'
        },
        component: ProfileComponent,
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'appointments',
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_ADMIN'],
            pageTitle: 'appointment.managment.home.title',
            defaultSort: 'date,desc'
        },
        component: AppointmentComponent,
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'my-appointments',
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'appointment.managment.myappointments.title',
            defaultSort: 'date,desc'
        },
        component: AppointmentComponent,
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'doctor/appointments',
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_DOCTOR'],
            pageTitle: 'appointment.managment.myappointmentsasdoctor.title',
            defaultSort: 'date,desc'
        },
        component: AppointmentComponent,
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'appointment/take',
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'appointment.managment.take.title'
        },
        component: TakeAppointmentComponent,
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'appointment/:appointment_id/view',
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'appointment.managment.view.title'
        },
        component: ViewAppointmentComponent,
        canActivate: [UserRouteAccessService]
    }
];
