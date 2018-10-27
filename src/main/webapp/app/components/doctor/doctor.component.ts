import { Component, OnDestroy, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';

import { ActivatedRoute, Router } from '@angular/router';
import { JhiAlertService, JhiParseLinks } from 'ng-jhipster';

import { ITEMS_PER_PAGE } from 'app/shared';
import { Principal } from 'app/core';
import { Doctor, DoctorService } from 'app/components';
import { UserMinService } from 'app/components/userMin/user.min.service';

@Component({
    selector: 'jhi-doctor-list',
    templateUrl: './doctor.component.html',
    styles: []
})
export class DoctorComponent implements OnInit, OnDestroy {
    doctors: Doctor[];
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

    constructor(
        private doctorService: DoctorService,
        private userMinService: UserMinService,
        private alertService: JhiAlertService,
        private principal: Principal,
        private parseLinks: JhiParseLinks,
        private activatedRoute: ActivatedRoute,
        private router: Router
    ) {
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
            this.loadAll();
        });
    }

    ngOnDestroy() {
        this.routeData.unsubscribe();
    }

    loadAll() {
        this.doctorService
            .query({
                page: this.page - 1,
                size: this.itemsPerPage,
                sort: this.sort()
            })
            .subscribe(
                (res: HttpResponse<Doctor[]>) => this.onSuccess(res.body, res.headers),
                (res: HttpResponse<any>) => this.onError(res.body)
            );
    }

    trackIdentity(index, item: Doctor) {
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
        this.router.navigate(['/doctors'], {
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
        this.doctors = data;
        this.doctors = this.doctors.map(doctor => {
            this.userMinService.find(doctor.userId).subscribe(response => {
                if (response.status === 200) {
                    doctor.firstName = response.body.firstName;
                    doctor.lastName = response.body.lastName;
                    doctor.email = response.body.email;
                    doctor.imageUrl = response.body.imageUrl;
                }
            });
            return doctor;
        });
    }

    private onError(error) {
        this.alertService.error(error.error, error.message, null);
    }
}
