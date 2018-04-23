import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { CuentaApp } from './cuenta-app.model';
import { createRequestOption } from '../../shared';

export type EntityResponseType = HttpResponse<CuentaApp>;

@Injectable()
export class CuentaAppService {

    private resourceUrl =  SERVER_API_URL + 'api/cuenta-apps';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/cuenta-apps';

    constructor(private http: HttpClient) { }

    create(cuentaApp: CuentaApp): Observable<EntityResponseType> {
        const copy = this.convert(cuentaApp);
        return this.http.post<CuentaApp>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    update(cuentaApp: CuentaApp): Observable<EntityResponseType> {
        const copy = this.convert(cuentaApp);
        return this.http.put<CuentaApp>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<CuentaApp>(`${this.resourceUrl}/${id}`, { observe: 'response'})
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    query(req?: any): Observable<HttpResponse<CuentaApp[]>> {
        const options = createRequestOption(req);
        return this.http.get<CuentaApp[]>(this.resourceUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<CuentaApp[]>) => this.convertArrayResponse(res));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response'});
    }

    search(req?: any): Observable<HttpResponse<CuentaApp[]>> {
        const options = createRequestOption(req);
        return this.http.get<CuentaApp[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<CuentaApp[]>) => this.convertArrayResponse(res));
    }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: CuentaApp = this.convertItemFromServer(res.body);
        return res.clone({body});
    }

    private convertArrayResponse(res: HttpResponse<CuentaApp[]>): HttpResponse<CuentaApp[]> {
        const jsonResponse: CuentaApp[] = res.body;
        const body: CuentaApp[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({body});
    }

    /**
     * Convert a returned JSON object to CuentaApp.
     */
    private convertItemFromServer(cuentaApp: CuentaApp): CuentaApp {
        const copy: CuentaApp = Object.assign({}, cuentaApp);
        return copy;
    }

    /**
     * Convert a CuentaApp to a JSON which can be sent to the server.
     */
    private convert(cuentaApp: CuentaApp): CuentaApp {
        const copy: CuentaApp = Object.assign({}, cuentaApp);
        return copy;
    }
}
