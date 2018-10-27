import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { IUserMin } from 'app/components/userMin/user.min.model';

@Injectable({ providedIn: 'root' })
export class UserMinService {
    private resourceUrl = SERVER_API_URL + 'api/usersmin';

    constructor(private http: HttpClient) {}

    find(id: any): Observable<HttpResponse<IUserMin>> {
        return this.http.get<IUserMin>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }
}
