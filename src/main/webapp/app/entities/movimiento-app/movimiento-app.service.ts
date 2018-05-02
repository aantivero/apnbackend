import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { JhiDateUtils } from 'ng-jhipster';

import { MovimientoApp } from './movimiento-app.model';
import { createRequestOption } from '../../shared';

export type EntityResponseType = HttpResponse<MovimientoApp>;

@Injectable()
export class MovimientoAppService {

    private resourceUrl =  SERVER_API_URL + 'api/movimiento-apps';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/movimiento-apps';

    constructor(private http: HttpClient, private dateUtils: JhiDateUtils) { }

    create(movimientoApp: MovimientoApp): Observable<EntityResponseType> {
        const copy = this.convert(movimientoApp);
        return this.http.post<MovimientoApp>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    update(movimientoApp: MovimientoApp): Observable<EntityResponseType> {
        const copy = this.convert(movimientoApp);
        return this.http.put<MovimientoApp>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<MovimientoApp>(`${this.resourceUrl}/${id}`, { observe: 'response'})
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    query(req?: any): Observable<HttpResponse<MovimientoApp[]>> {
        const options = createRequestOption(req);
        return this.http.get<MovimientoApp[]>(this.resourceUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<MovimientoApp[]>) => this.convertArrayResponse(res));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response'});
    }

    search(req?: any): Observable<HttpResponse<MovimientoApp[]>> {
        const options = createRequestOption(req);
        return this.http.get<MovimientoApp[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<MovimientoApp[]>) => this.convertArrayResponse(res));
    }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: MovimientoApp = this.convertItemFromServer(res.body);
        return res.clone({body});
    }

    private convertArrayResponse(res: HttpResponse<MovimientoApp[]>): HttpResponse<MovimientoApp[]> {
        const jsonResponse: MovimientoApp[] = res.body;
        const body: MovimientoApp[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({body});
    }

    /**
     * Convert a returned JSON object to MovimientoApp.
     */
    private convertItemFromServer(movimientoApp: MovimientoApp): MovimientoApp {
        const copy: MovimientoApp = Object.assign({}, movimientoApp);
        copy.timestamp = this.dateUtils
            .convertDateTimeFromServer(movimientoApp.timestamp);
        copy.consolidadoTimestamp = this.dateUtils
            .convertDateTimeFromServer(movimientoApp.consolidadoTimestamp);
        return copy;
    }

    /**
     * Convert a MovimientoApp to a JSON which can be sent to the server.
     */
    private convert(movimientoApp: MovimientoApp): MovimientoApp {
        const copy: MovimientoApp = Object.assign({}, movimientoApp);

        copy.timestamp = this.dateUtils.toDate(movimientoApp.timestamp);

        copy.consolidadoTimestamp = this.dateUtils.toDate(movimientoApp.consolidadoTimestamp);
        return copy;
    }
}
