import { Routes } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import {
    AppointmentComponent,
    DoctorComponent,
    ProfileComponent,
    TakeAppointmentComponent,
    UpdateAppointmentComponent,
    ViewAppointmentComponent
} from './';
import { UserRouteAccessService } from 'app/core';

export const componentsState: Routes = [
    {
        path: '',
        children: [
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
                path: 'appointment/:appointment_id/update',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'appointment.managment.update.title'
                },
                component: UpdateAppointmentComponent,
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
        ]
    }
];
