import { Component, OnDestroy, OnInit } from '@angular/core';
import { Appointment, AppointmentService, CancelAppointmentComponent, ViewAppointmentComponent } from 'app/components';
import { JhiAlertService, JhiEventManager, JhiParseLinks } from 'ng-jhipster';
import { ActivatedRoute, Router } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { Principal } from 'app/core';
import { HttpResponse } from '@angular/common/http';
import { ITEMS_PER_PAGE } from 'app/shared';

@Component({
    selector: 'jhi-appointment',
    templateUrl: './appointment.component.html',
    styles: []
})
export class AppointmentComponent implements OnInit, OnDestroy {
    appointments: Appointment[];
    error: any;
    success: any;
    routeData: any;
    links: any;
    totalItems: any;
    queryCount: any;
    itemsPerPage: any;
    page: any;
    predicate: any;
    previousPage: any;
    reverse: any;
    loadBy: any;
    account: any;

    constructor(
        private appointmentService: AppointmentService,
        private alertService: JhiAlertService,
        private principal: Principal,
        private parseLinks: JhiParseLinks,
        private activatedRoute: ActivatedRoute,
        private router: Router,
        private eventManager: JhiEventManager,
        private modalService: NgbModal
    ) {
        if (activatedRoute.snapshot.routeConfig.path === 'appointments') {
            this.loadBy = 'all';
        } else if (activatedRoute.snapshot.routeConfig.path === 'my-appointments') {
            this.loadBy = 'my';
        } else {
            this.loadBy = 'doctor';
        }
        this.itemsPerPage = ITEMS_PER_PAGE;
        this.routeData = this.activatedRoute.data.subscribe(data => {
            this.page = data['pagingParams'].page;
            this.previousPage = data['pagingParams'].page;
            this.reverse = data['pagingParams'].ascending;
            this.predicate = data['pagingParams'].predicate;
        });
    }

    ngOnInit() {
        this.principal.identity().then(account => {
            this.account = account;
            console.log(this.account);
            this.loadAll();
            this.registerChangeInAppointments();
        });
    }

    ngOnDestroy() {
        this.routeData.unsubscribe();
    }

    registerChangeInAppointments() {
        this.eventManager.subscribe('AppointmentListModification', this.loadAll());
    }

    loadAll() {
        const queryParams: any = {
            page: this.page - 1,
            size: this.itemsPerPage,
            sort: this.sort()
        };
        let query: any;
        if (this.loadBy === 'all') {
            query = this.appointmentService.queryAll(queryParams);
        } else if (this.loadBy === 'my') {
            query = this.appointmentService.queryByPatient(queryParams, this.account.id);
        } else {
            query = this.appointmentService.queryByDoctor(queryParams, this.account.id);
        }
        query.subscribe(
            (res: HttpResponse<Appointment[]>) => this.onSuccess(res.body, res.headers),
            (res: HttpResponse<any>) => this.onError(res.body)
        );
    }

    trackIdentity(index, item: Appointment) {
        return item.id;
    }

    sort() {
        const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
        if (this.predicate !== 'id') {
            result.push('id');
        }
        return result;
    }

    loadPage(page: number) {
        if (page !== this.previousPage) {
            this.previousPage = page;
            this.transition();
        }
    }

    transition() {
        console.clear();
        console.log(this.activatedRoute.snapshot);
        this.router.navigate([this.activatedRoute.snapshot.url], {
            queryParams: {
                page: this.page,
                sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
            }
        });
        this.loadAll();
    }

    private onSuccess(data, headers) {
        this.links = this.parseLinks.parse(headers.get('link'));
        this.totalItems = headers.get('X-Total-Count');
        this.queryCount = this.totalItems;
        this.appointments = data;
    }

    private onError(error) {
        this.alertService.error(error.error, error.message, null);
    }

    cancelAppointment(appointment: Appointment) {
        const modalRef = this.modalService.open(CancelAppointmentComponent, { size: 'lg', backdrop: 'static' });
        modalRef.componentInstance.appointment = appointment;
        modalRef.result.then();
    }

    viewAppointment(appointment: Appointment) {
        const modalRef = this.modalService.open(ViewAppointmentComponent, { size: 'lg', backdrop: 'static' });
        modalRef.componentInstance.appointment = appointment;
        modalRef.result.then();
    }
}
