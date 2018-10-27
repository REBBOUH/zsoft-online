import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IAppointment } from 'app/components';

@Injectable({ providedIn: 'root' })
@Injectable({
    providedIn: 'root'
})
export class AppointmentService {
    private resourceUrl = SERVER_API_URL + 'api/appointments';

    constructor(private http: HttpClient) {}

    take(appointment: IAppointment): Observable<HttpResponse<IAppointment>> {
        return this.http.post<IAppointment>(this.resourceUrl, appointment, { observe: 'response' });
    }

    update(appointment: IAppointment): Observable<HttpResponse<IAppointment>> {
        return this.http.put<IAppointment>(this.resourceUrl, appointment, { observe: 'response' });
    }

    cancel(id: any): Observable<HttpResponse<any>> {
        return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    find(id: any): Observable<HttpResponse<IAppointment>> {
        return this.http.get<IAppointment>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    queryAll(req?: any): Observable<HttpResponse<IAppointment[]>> {
        const options = createRequestOption(req);
        return this.http.get<IAppointment[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    queryByPatient(req: any, patient_id: any): Observable<HttpResponse<IAppointment[]>> {
        const options = createRequestOption(req);
        return this.http.get<IAppointment[]>(`${this.resourceUrl}/patient/${patient_id}`, { params: options, observe: 'response' });
    }

    queryByDoctor(req: any, doctor_user_id): Observable<HttpResponse<IAppointment[]>> {
        const options = createRequestOption(req);
        return this.http.get<IAppointment[]>(`${this.resourceUrl}/doctor/${doctor_user_id}`, { params: options, observe: 'response' });
    }

    available(doctor_id: any, date: Date): Observable<HttpResponse<IAppointment[]>> {
        return this.http.get<IAppointment[]>(`${this.resourceUrl}/available/${doctor_id}/${date}`, { observe: 'response' });
    }
}
