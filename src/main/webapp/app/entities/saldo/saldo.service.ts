import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { JhiDateUtils } from 'ng-jhipster';

import { Saldo } from './saldo.model';
import { createRequestOption } from '../../shared';

export type EntityResponseType = HttpResponse<Saldo>;

@Injectable()
export class SaldoService {

    private resourceUrl =  SERVER_API_URL + 'api/saldos';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/saldos';

    constructor(private http: HttpClient, private dateUtils: JhiDateUtils) { }

    create(saldo: Saldo): Observable<EntityResponseType> {
        const copy = this.convert(saldo);
        return this.http.post<Saldo>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    update(saldo: Saldo): Observable<EntityResponseType> {
        const copy = this.convert(saldo);
        return this.http.put<Saldo>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<Saldo>(`${this.resourceUrl}/${id}`, { observe: 'response'})
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    query(req?: any): Observable<HttpResponse<Saldo[]>> {
        const options = createRequestOption(req);
        return this.http.get<Saldo[]>(this.resourceUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<Saldo[]>) => this.convertArrayResponse(res));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response'});
    }

    search(req?: any): Observable<HttpResponse<Saldo[]>> {
        const options = createRequestOption(req);
        return this.http.get<Saldo[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<Saldo[]>) => this.convertArrayResponse(res));
    }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: Saldo = this.convertItemFromServer(res.body);
        return res.clone({body});
    }

    private convertArrayResponse(res: HttpResponse<Saldo[]>): HttpResponse<Saldo[]> {
        const jsonResponse: Saldo[] = res.body;
        const body: Saldo[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({body});
    }

    /**
     * Convert a returned JSON object to Saldo.
     */
    private convertItemFromServer(saldo: Saldo): Saldo {
        const copy: Saldo = Object.assign({}, saldo);
        copy.fechaCreacion = this.dateUtils
            .convertDateTimeFromServer(saldo.fechaCreacion);
        copy.fechaModificacion = this.dateUtils
            .convertDateTimeFromServer(saldo.fechaModificacion);
        return copy;
    }

    /**
     * Convert a Saldo to a JSON which can be sent to the server.
     */
    private convert(saldo: Saldo): Saldo {
        const copy: Saldo = Object.assign({}, saldo);

        copy.fechaCreacion = this.dateUtils.toDate(saldo.fechaCreacion);

        copy.fechaModificacion = this.dateUtils.toDate(saldo.fechaModificacion);
        return copy;
    }
}
