import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { App } from './app.model';
import { createRequestOption } from '../../shared';

export type EntityResponseType = HttpResponse<App>;

@Injectable()
export class AppService {

    private resourceUrl =  SERVER_API_URL + 'api/apps';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/apps';

    constructor(private http: HttpClient) { }

    create(app: App): Observable<EntityResponseType> {
        const copy = this.convert(app);
        return this.http.post<App>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    update(app: App): Observable<EntityResponseType> {
        const copy = this.convert(app);
        return this.http.put<App>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<App>(`${this.resourceUrl}/${id}`, { observe: 'response'})
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    query(req?: any): Observable<HttpResponse<App[]>> {
        const options = createRequestOption(req);
        return this.http.get<App[]>(this.resourceUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<App[]>) => this.convertArrayResponse(res));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response'});
    }

    search(req?: any): Observable<HttpResponse<App[]>> {
        const options = createRequestOption(req);
        return this.http.get<App[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<App[]>) => this.convertArrayResponse(res));
    }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: App = this.convertItemFromServer(res.body);
        return res.clone({body});
    }

    private convertArrayResponse(res: HttpResponse<App[]>): HttpResponse<App[]> {
        const jsonResponse: App[] = res.body;
        const body: App[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({body});
    }

    /**
     * Convert a returned JSON object to App.
     */
    private convertItemFromServer(app: App): App {
        const copy: App = Object.assign({}, app);
        return copy;
    }

    /**
     * Convert a App to a JSON which can be sent to the server.
     */
    private convert(app: App): App {
        const copy: App = Object.assign({}, app);
        return copy;
    }
}
