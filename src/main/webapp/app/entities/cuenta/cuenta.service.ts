import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { JhiDateUtils } from 'ng-jhipster';

import { Cuenta } from './cuenta.model';
import { createRequestOption } from '../../shared';

export type EntityResponseType = HttpResponse<Cuenta>;

@Injectable()
export class CuentaService {

    private resourceUrl =  SERVER_API_URL + 'api/cuentas';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/cuentas';

    constructor(private http: HttpClient, private dateUtils: JhiDateUtils) { }

    create(cuenta: Cuenta): Observable<EntityResponseType> {
        const copy = this.convert(cuenta);
        return this.http.post<Cuenta>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    update(cuenta: Cuenta): Observable<EntityResponseType> {
        const copy = this.convert(cuenta);
        return this.http.put<Cuenta>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<Cuenta>(`${this.resourceUrl}/${id}`, { observe: 'response'})
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    query(req?: any): Observable<HttpResponse<Cuenta[]>> {
        const options = createRequestOption(req);
        return this.http.get<Cuenta[]>(this.resourceUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<Cuenta[]>) => this.convertArrayResponse(res));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response'});
    }

    search(req?: any): Observable<HttpResponse<Cuenta[]>> {
        const options = createRequestOption(req);
        return this.http.get<Cuenta[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<Cuenta[]>) => this.convertArrayResponse(res));
    }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: Cuenta = this.convertItemFromServer(res.body);
        return res.clone({body});
    }

    private convertArrayResponse(res: HttpResponse<Cuenta[]>): HttpResponse<Cuenta[]> {
        const jsonResponse: Cuenta[] = res.body;
        const body: Cuenta[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({body});
    }

    /**
     * Convert a returned JSON object to Cuenta.
     */
    private convertItemFromServer(cuenta: Cuenta): Cuenta {
        const copy: Cuenta = Object.assign({}, cuenta);
        copy.fechaCreacion = this.dateUtils
            .convertDateTimeFromServer(cuenta.fechaCreacion);
        copy.fechaModificacion = this.dateUtils
            .convertDateTimeFromServer(cuenta.fechaModificacion);
        return copy;
    }

    /**
     * Convert a Cuenta to a JSON which can be sent to the server.
     */
    private convert(cuenta: Cuenta): Cuenta {
        const copy: Cuenta = Object.assign({}, cuenta);

        copy.fechaCreacion = this.dateUtils.toDate(cuenta.fechaCreacion);

        copy.fechaModificacion = this.dateUtils.toDate(cuenta.fechaModificacion);
        return copy;
    }
}
