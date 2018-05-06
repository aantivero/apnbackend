import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { UserExtra } from './user-extra.model';
import { createRequestOption } from '../../shared';

export type EntityResponseType = HttpResponse<UserExtra>;

@Injectable()
export class UserExtraService {

    private resourceUrl =  SERVER_API_URL + 'api/user-extras';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/user-extras';

    constructor(private http: HttpClient) { }

    create(userExtra: UserExtra): Observable<EntityResponseType> {
        const copy = this.convert(userExtra);
        return this.http.post<UserExtra>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    update(userExtra: UserExtra): Observable<EntityResponseType> {
        const copy = this.convert(userExtra);
        return this.http.put<UserExtra>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<UserExtra>(`${this.resourceUrl}/${id}`, { observe: 'response'})
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    query(req?: any): Observable<HttpResponse<UserExtra[]>> {
        const options = createRequestOption(req);
        return this.http.get<UserExtra[]>(this.resourceUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<UserExtra[]>) => this.convertArrayResponse(res));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response'});
    }

    search(req?: any): Observable<HttpResponse<UserExtra[]>> {
        const options = createRequestOption(req);
        return this.http.get<UserExtra[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<UserExtra[]>) => this.convertArrayResponse(res));
    }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: UserExtra = this.convertItemFromServer(res.body);
        return res.clone({body});
    }

    private convertArrayResponse(res: HttpResponse<UserExtra[]>): HttpResponse<UserExtra[]> {
        const jsonResponse: UserExtra[] = res.body;
        const body: UserExtra[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({body});
    }

    /**
     * Convert a returned JSON object to UserExtra.
     */
    private convertItemFromServer(userExtra: UserExtra): UserExtra {
        const copy: UserExtra = Object.assign({}, userExtra);
        return copy;
    }

    /**
     * Convert a UserExtra to a JSON which can be sent to the server.
     */
    private convert(userExtra: UserExtra): UserExtra {
        const copy: UserExtra = Object.assign({}, userExtra);
        return copy;
    }
}
