import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { Doctor, IDoctor } from 'app/components';

@Injectable({ providedIn: 'root' })
export class DoctorService {
    private resourceUrl = SERVER_API_URL + 'api/doctors';

    constructor(private http: HttpClient) {}

    create(doctor: IDoctor): Observable<HttpResponse<IDoctor>> {
        return this.http.post<IDoctor>(this.resourceUrl, doctor, { observe: 'response' });
    }

    update(doctor: IDoctor): Observable<HttpResponse<IDoctor>> {
        return this.http.put<IDoctor>(this.resourceUrl, doctor, { observe: 'response' });
    }

    find(id: any): Observable<HttpResponse<IDoctor>> {
        return this.http.get<IDoctor>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    findByUserId(id: any): Observable<HttpResponse<Doctor>> {
        return this.http.get<Doctor>(`${this.resourceUrl}/user/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<HttpResponse<IDoctor[]>> {
        const options = createRequestOption(req);
        return this.http.get<IDoctor[]>(this.resourceUrl, { params: options, observe: 'response' });
    }
}
