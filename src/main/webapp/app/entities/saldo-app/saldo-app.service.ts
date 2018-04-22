import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { SaldoApp } from './saldo-app.model';
import { createRequestOption } from '../../shared';

export type EntityResponseType = HttpResponse<SaldoApp>;

@Injectable()
export class SaldoAppService {

    private resourceUrl =  SERVER_API_URL + 'api/saldo-apps';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/saldo-apps';

    constructor(private http: HttpClient) { }

    create(saldoApp: SaldoApp): Observable<EntityResponseType> {
        const copy = this.convert(saldoApp);
        return this.http.post<SaldoApp>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    update(saldoApp: SaldoApp): Observable<EntityResponseType> {
        const copy = this.convert(saldoApp);
        return this.http.put<SaldoApp>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<SaldoApp>(`${this.resourceUrl}/${id}`, { observe: 'response'})
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    query(req?: any): Observable<HttpResponse<SaldoApp[]>> {
        const options = createRequestOption(req);
        return this.http.get<SaldoApp[]>(this.resourceUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<SaldoApp[]>) => this.convertArrayResponse(res));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response'});
    }

    search(req?: any): Observable<HttpResponse<SaldoApp[]>> {
        const options = createRequestOption(req);
        return this.http.get<SaldoApp[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<SaldoApp[]>) => this.convertArrayResponse(res));
    }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: SaldoApp = this.convertItemFromServer(res.body);
        return res.clone({body});
    }

    private convertArrayResponse(res: HttpResponse<SaldoApp[]>): HttpResponse<SaldoApp[]> {
        const jsonResponse: SaldoApp[] = res.body;
        const body: SaldoApp[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({body});
    }

    /**
     * Convert a returned JSON object to SaldoApp.
     */
    private convertItemFromServer(saldoApp: SaldoApp): SaldoApp {
        const copy: SaldoApp = Object.assign({}, saldoApp);
        return copy;
    }

    /**
     * Convert a SaldoApp to a JSON which can be sent to the server.
     */
    private convert(saldoApp: SaldoApp): SaldoApp {
        const copy: SaldoApp = Object.assign({}, saldoApp);
        return copy;
    }
}
