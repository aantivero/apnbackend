import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { Banco } from './banco.model';
import { createRequestOption } from '../../shared';

export type EntityResponseType = HttpResponse<Banco>;

@Injectable()
export class BancoService {

    private resourceUrl =  SERVER_API_URL + 'api/bancos';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/bancos';

    constructor(private http: HttpClient) { }

    create(banco: Banco): Observable<EntityResponseType> {
        const copy = this.convert(banco);
        return this.http.post<Banco>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    update(banco: Banco): Observable<EntityResponseType> {
        const copy = this.convert(banco);
        return this.http.put<Banco>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<Banco>(`${this.resourceUrl}/${id}`, { observe: 'response'})
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    query(req?: any): Observable<HttpResponse<Banco[]>> {
        const options = createRequestOption(req);
        return this.http.get<Banco[]>(this.resourceUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<Banco[]>) => this.convertArrayResponse(res));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response'});
    }

    search(req?: any): Observable<HttpResponse<Banco[]>> {
        const options = createRequestOption(req);
        return this.http.get<Banco[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<Banco[]>) => this.convertArrayResponse(res));
    }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: Banco = this.convertItemFromServer(res.body);
        return res.clone({body});
    }

    private convertArrayResponse(res: HttpResponse<Banco[]>): HttpResponse<Banco[]> {
        const jsonResponse: Banco[] = res.body;
        const body: Banco[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({body});
    }

    /**
     * Convert a returned JSON object to Banco.
     */
    private convertItemFromServer(banco: Banco): Banco {
        const copy: Banco = Object.assign({}, banco);
        return copy;
    }

    /**
     * Convert a Banco to a JSON which can be sent to the server.
     */
    private convert(banco: Banco): Banco {
        const copy: Banco = Object.assign({}, banco);
        return copy;
    }
}
