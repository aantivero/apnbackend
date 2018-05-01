import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { JhiDateUtils } from 'ng-jhipster';

import { TransferenciaApp } from './transferencia-app.model';
import { createRequestOption } from '../../shared';

export type EntityResponseType = HttpResponse<TransferenciaApp>;

@Injectable()
export class TransferenciaAppService {

    private resourceUrl =  SERVER_API_URL + 'api/transferencia-apps';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/transferencia-apps';

    constructor(private http: HttpClient, private dateUtils: JhiDateUtils) { }

    create(transferenciaApp: TransferenciaApp): Observable<EntityResponseType> {
        const copy = this.convert(transferenciaApp);
        return this.http.post<TransferenciaApp>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    update(transferenciaApp: TransferenciaApp): Observable<EntityResponseType> {
        const copy = this.convert(transferenciaApp);
        return this.http.put<TransferenciaApp>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<TransferenciaApp>(`${this.resourceUrl}/${id}`, { observe: 'response'})
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    query(req?: any): Observable<HttpResponse<TransferenciaApp[]>> {
        const options = createRequestOption(req);
        return this.http.get<TransferenciaApp[]>(this.resourceUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<TransferenciaApp[]>) => this.convertArrayResponse(res));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response'});
    }

    search(req?: any): Observable<HttpResponse<TransferenciaApp[]>> {
        const options = createRequestOption(req);
        return this.http.get<TransferenciaApp[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<TransferenciaApp[]>) => this.convertArrayResponse(res));
    }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: TransferenciaApp = this.convertItemFromServer(res.body);
        return res.clone({body});
    }

    private convertArrayResponse(res: HttpResponse<TransferenciaApp[]>): HttpResponse<TransferenciaApp[]> {
        const jsonResponse: TransferenciaApp[] = res.body;
        const body: TransferenciaApp[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({body});
    }

    /**
     * Convert a returned JSON object to TransferenciaApp.
     */
    private convertItemFromServer(transferenciaApp: TransferenciaApp): TransferenciaApp {
        const copy: TransferenciaApp = Object.assign({}, transferenciaApp);
        copy.timestamp = this.dateUtils
            .convertDateTimeFromServer(transferenciaApp.timestamp);
        return copy;
    }

    /**
     * Convert a TransferenciaApp to a JSON which can be sent to the server.
     */
    private convert(transferenciaApp: TransferenciaApp): TransferenciaApp {
        const copy: TransferenciaApp = Object.assign({}, transferenciaApp);

        copy.timestamp = this.dateUtils.toDate(transferenciaApp.timestamp);
        return copy;
    }
}
