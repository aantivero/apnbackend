import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { CargarSaldo } from './cargar-saldo.model';
import { createRequestOption } from '../../shared';

export type EntityResponseType = HttpResponse<CargarSaldo>;

@Injectable()
export class CargarSaldoService {

    private resourceUrl =  SERVER_API_URL + 'api/cargar-saldos';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/cargar-saldos';

    constructor(private http: HttpClient) { }

    create(cargarSaldo: CargarSaldo): Observable<EntityResponseType> {
        const copy = this.convert(cargarSaldo);
        return this.http.post<CargarSaldo>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    update(cargarSaldo: CargarSaldo): Observable<EntityResponseType> {
        const copy = this.convert(cargarSaldo);
        return this.http.put<CargarSaldo>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<CargarSaldo>(`${this.resourceUrl}/${id}`, { observe: 'response'})
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    query(req?: any): Observable<HttpResponse<CargarSaldo[]>> {
        const options = createRequestOption(req);
        return this.http.get<CargarSaldo[]>(this.resourceUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<CargarSaldo[]>) => this.convertArrayResponse(res));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response'});
    }

    search(req?: any): Observable<HttpResponse<CargarSaldo[]>> {
        const options = createRequestOption(req);
        return this.http.get<CargarSaldo[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<CargarSaldo[]>) => this.convertArrayResponse(res));
    }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: CargarSaldo = this.convertItemFromServer(res.body);
        return res.clone({body});
    }

    private convertArrayResponse(res: HttpResponse<CargarSaldo[]>): HttpResponse<CargarSaldo[]> {
        const jsonResponse: CargarSaldo[] = res.body;
        const body: CargarSaldo[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({body});
    }

    /**
     * Convert a returned JSON object to CargarSaldo.
     */
    private convertItemFromServer(cargarSaldo: CargarSaldo): CargarSaldo {
        const copy: CargarSaldo = Object.assign({}, cargarSaldo);
        return copy;
    }

    /**
     * Convert a CargarSaldo to a JSON which can be sent to the server.
     */
    private convert(cargarSaldo: CargarSaldo): CargarSaldo {
        const copy: CargarSaldo = Object.assign({}, cargarSaldo);
        return copy;
    }
}
